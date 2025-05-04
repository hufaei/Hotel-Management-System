package com.sz.admin.payment.service.impl;

import com.botsuch.rpcstarter.annotation.RpcService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.sz.admin.bookings.mapper.BookingsMapper;
import com.sz.admin.bookings.pojo.po.Bookings;
import com.sz.admin.bookings.service.BookingsService;
import com.sz.admin.users.service.UsersService;
import com.sz.core.common.event.EventPublisher;
import com.sz.platform.enums.BookingStatus;
import com.sz.platform.enums.PaymentStatus;
import com.sz.platform.event.PaymentCancelledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.sz.admin.payment.service.PaymentService;
import com.sz.admin.payment.pojo.po.Payment;
import com.sz.admin.payment.mapper.PaymentMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.query.QueryChain;
import com.sz.core.common.enums.CommonResponseEnum;
import com.sz.core.util.PageUtils;
import com.sz.core.util.BeanCopyUtils;
import com.sz.core.util.Utils;
import com.sz.core.common.entity.PageResult;
import com.sz.core.common.entity.SelectIdsDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import com.sz.admin.payment.pojo.dto.PaymentCreateDTO;
import com.sz.admin.payment.pojo.dto.PaymentUpdateDTO;
import com.sz.admin.payment.pojo.dto.PaymentListDTO;
import com.sz.admin.payment.pojo.vo.PaymentVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 支付单表 服务实现类
 * </p>
 *
 * @author botsuchi
 * @since 2024-11-23
 */
@Service
@RequiredArgsConstructor
@Slf4j
@RpcService
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {
    private final EventPublisher eventPublisher;
    private final UsersService usersService;
    private final BookingsMapper bookingsMapper;

    @Override
    public void create(PaymentCreateDTO dto){
        Payment payment = BeanCopyUtils.copy(dto, Payment.class);
        long count;
        // 唯一性校验
        count = QueryChain.of(Payment.class).eq(Payment::getBookingId, dto.getBookingId()).count();
        CommonResponseEnum.EXISTS.message("此订单已存在支付流水").assertTrue(count > 0);
        // 未支付状态
        payment.setPaymentStatus(PaymentStatus.UNPAID);
        save(payment);
    }
    @Override
    @Transactional
    // 已支付了调用这个
    public Boolean cancel(PaymentUpdateDTO dto){
        // id有效性校验
        QueryWrapper wrapper = QueryWrapper.create()
                .eq(Payment::getPaymentId, dto.getPaymentId());
        CommonResponseEnum.INVALID_ID.message("支付单不存在").assertTrue(count(wrapper) <= 0);
        Payment payment = getOne(wrapper);
        // 已经支付--退款+取消订单
        if(payment.getPaymentStatus().equals(PaymentStatus.PAID)){
            // todo 退款逻辑
        }

        // 改状态+改预订单状态
        if(payment.getPaymentStatus().equals(PaymentStatus.CANCEL)){
            return true;
        }
        payment.setPaymentStatus(PaymentStatus.CANCEL);
        updateById(payment);
        // 事件发布解耦
        eventPublisher.publish(new PaymentCancelledEvent(this, payment.getBookingId()));

        return true;

    }
    @Override
    public void finished(PaymentUpdateDTO dto){
        // id有效性校验
        QueryWrapper wrapper = QueryWrapper.create()
            .eq(Payment::getPaymentId, dto.getPaymentId());
        CommonResponseEnum.INVALID_ID.message("支付单不存在").assertTrue(count(wrapper) <= 0);
        Payment payment = getOne(wrapper);
        CommonResponseEnum.INVALID.message("不允许的操作")
                .assertTrue(payment.getPaymentStatus().equals(PaymentStatus.CANCEL));
        if(payment.getPaymentStatus().equals(PaymentStatus.FINISHED)){
            return;
        }
        payment.setPaymentStatus(PaymentStatus.FINISHED);
        saveOrUpdate(payment);
    }

    @Override
    public PageResult<PaymentVO> page(PaymentListDTO dto){
        Page<PaymentVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), PaymentVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<PaymentVO> list(PaymentListDTO dto){
        return listAs(buildQueryWrapper(dto), PaymentVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto){
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    public PaymentVO detail(Object id){
        Payment payment = getById((Serializable) id);
        CommonResponseEnum.INVALID_ID.assertNull(payment);
        return BeanCopyUtils.copy(payment, PaymentVO.class);
    }

    /**
     * Try 阶段：校验支付单有效性 + 冻结余额 + 标记预扣
     */
    @Override
    @Transactional
    public void paid(PaymentUpdateDTO dto) {
        // id有效性校验
        QueryWrapper wrapper = QueryWrapper.create()
                .eq(Payment::getPaymentId, dto.getPaymentId());
        CommonResponseEnum.INVALID_ID.message("支付单不存在").assertTrue(count(wrapper) <= 0);
        Payment payment = getOne(wrapper);
        CommonResponseEnum.INVALID.message("不允许的操作")
                .assertFalse(payment.getPaymentStatus().equals(PaymentStatus.UNPAID));
        payment.setPaymentStatus(PaymentStatus.PAID);
        QueryWrapper bwrapper = QueryWrapper.create().from(Bookings.class);
        bwrapper.eq(Bookings::getBookingId, payment.getBookingId());
        Bookings bookings = bookingsMapper.selectOneByQuery(bwrapper);
        bookings.setStatus(BookingStatus.CONFIRMED);
//        usersService.freeze(BigDecimal.valueOf(payment.getAmount()));
//        // 3. 更新支付单状态为 PREPAID（预支付）
//        payment.setPaymentStatus(PaymentStatus.PREPAID);
        usersService.paid(BigDecimal.valueOf(payment.getAmount()));
        saveOrUpdate(payment);
    }

//    /**
//     * Confirm 阶段：实际扣款 + 标记已支付
//     */
//    @Transactional
//    public void confirmPaid(PaymentUpdateDTO dto) {
//        // 幂等校验：若已扣款则跳过
//        QueryWrapper wrapper = QueryWrapper.create()
//                .eq(Payment::getPaymentId, dto.getPaymentId());
//        CommonResponseEnum.INVALID_ID.message("支付单不存在").assertTrue(count(wrapper) <= 0);
//        Payment payment = getOne(wrapper);
//        if(payment.getPaymentStatus().equals(PaymentStatus.PAID)){
//            return;
//        }
//        // 1. 从冻结中扣除余额
//        usersService.unfreeze(BigDecimal.valueOf(payment.getAmount()),Boolean.TRUE);
//
//        // 2. 更新支付单状态为 PAID
//        payment.setPaymentStatus(PaymentStatus.PAID);
//        saveOrUpdate(payment);
//    }
//
//    /**
//     * Cancel 阶段：解冻余额 + 回滚支付单状态
//     */
//    @Transactional
//    public void cancelPaid(PaymentUpdateDTO dto) {
//        // 幂等校验：若已扣款则跳过
//        QueryWrapper wrapper = QueryWrapper.create()
//                .eq(Payment::getPaymentId, dto.getPaymentId());
//        Payment payment = getOne(wrapper);
//        if(payment.getPaymentStatus().equals(PaymentStatus.FINISHED)){
//            return;
//        }
//        // 1. 解冻预扣余额
//        usersService.unfreeze(BigDecimal.valueOf(payment.getAmount()),Boolean.FALSE);
//
//        // 2. 回滚支付单状态为 UNPAID
//        payment.setPaymentStatus(PaymentStatus.UNPAID);
//       saveOrUpdate(payment);
//    }

    @Override
    @Transactional
    public PaymentVO detailByBookingId(Object id) {
        QueryWrapper wrapper = QueryWrapper.create()
                .eq(Payment::getBookingId, id);
        Payment payment = getOne(wrapper);
        CommonResponseEnum.INVALID_ID.assertNull(payment);
        return BeanCopyUtils.copy(payment, PaymentVO.class);
    }

    @Override
    public List<PaymentVO> detailByHotelId(String id) {
        QueryWrapper wrapper = QueryWrapper.create().from(Bookings.class);
        wrapper.eq(Bookings::getHotelId, id);
        List<Bookings> bookings = bookingsMapper.selectListByQuery(wrapper);
        List<Long> bookingIds = bookings.stream().map(Bookings::getBookingId).toList();
        QueryWrapper wrapper2 = QueryWrapper.create()
                .in(Payment::getBookingId, bookingIds)
                .orderBy(Payment::getCreatedAt,false)
                .eq(Payment::getPaymentStatus, PaymentStatus.FINISHED)
                .limit(0, 5);
        return listAs(wrapper2, PaymentVO.class);
    }

    private static QueryWrapper buildQueryWrapper(PaymentListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(Payment.class);
        if (Utils.isNotNull(dto.getPaymentId())) {
            wrapper.eq(Payment::getPaymentId, dto.getPaymentId());
        }
        if (Utils.isNotNull(dto.getBookingId())) {
            wrapper.eq(Payment::getBookingId, dto.getBookingId());
        }
        if (Utils.isNotNull(dto.getPaymentStatus())) {
            wrapper.eq(Payment::getPaymentStatus, dto.getPaymentStatus());
        }
        if (Utils.isNotNull(dto.getCreatedAtStart()) && Utils.isNotNull(dto.getCreatedAtEnd())) {
            wrapper.between(Payment::getCreatedAt, dto.getCreatedAtStart(), dto.getCreatedAtEnd());
        }
        wrapper.orderBy(Payment::getCreatedAt,false);
        return wrapper;
    }
}