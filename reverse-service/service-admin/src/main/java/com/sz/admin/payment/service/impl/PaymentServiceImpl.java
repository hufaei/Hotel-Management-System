package com.sz.admin.payment.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.sz.core.common.event.EventPublisher;
import com.sz.platform.enums.PaymentStatus;
import com.sz.platform.event.PaymentCancelledEvent;
import lombok.RequiredArgsConstructor;
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
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {
    private final EventPublisher eventPublisher;
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
    public Boolean cancel(PaymentUpdateDTO dto){
        // id有效性校验
        QueryWrapper wrapper = QueryWrapper.create()
                .eq(Payment::getPaymentId, dto.getPaymentId());
        CommonResponseEnum.INVALID_ID.message("支付单不存在").assertTrue(count(wrapper) <= 0);
        Payment payment = getOne(wrapper);
        // 已经支付--退款+取消订单
        if(payment.getPaymentStatus().equals(PaymentStatus.FINISHED)){
            // 退款逻辑
        }

        // 改状态+改预订单状态
        if(payment.getPaymentStatus().equals(PaymentStatus.CANCEL)){
            return true;
        }
        payment.setPaymentStatus(PaymentStatus.CANCEL);
        saveOrUpdate(payment);
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
        return wrapper;
    }
}