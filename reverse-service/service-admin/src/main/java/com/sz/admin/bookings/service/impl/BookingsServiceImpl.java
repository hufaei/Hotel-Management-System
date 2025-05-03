package com.sz.admin.bookings.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.sz.admin.bookings.pojo.dto.BookingsCancelDTO;
import com.sz.admin.hotelowners.pojo.po.HotelOwners;
import com.sz.admin.payment.pojo.dto.PaymentCreateDTO;
import com.sz.admin.payment.pojo.dto.PaymentUpdateDTO;
import com.sz.admin.payment.pojo.po.Payment;
import com.sz.admin.payment.service.PaymentService;
import com.sz.admin.rooms.pojo.dto.RoomsCreateDTO;
import com.sz.admin.rooms.pojo.dto.RoomsUpdateDTO;
import com.sz.admin.rooms.pojo.po.Rooms;
import com.sz.admin.rooms.service.RoomsService;
import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryBookDTO;
import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryCreateDTO;
import com.sz.admin.roomtypeinventory.service.RoomTypeInventoryService;
import com.sz.admin.roomtypes.pojo.po.RoomTypes;
import com.sz.admin.roomtypes.service.RoomTypesService;
import com.sz.core.common.constant.GlobalConstant;
import com.sz.core.common.event.EventPublisher;
import com.sz.platform.enums.BookingStatus;
import com.sz.platform.enums.PaymentStatus;
import com.sz.platform.event.PaymentCancelledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import com.sz.admin.bookings.service.BookingsService;
import com.sz.admin.bookings.pojo.po.Bookings;
import com.sz.admin.bookings.mapper.BookingsMapper;
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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import com.sz.admin.bookings.pojo.dto.BookingsCreateDTO;
import com.sz.admin.bookings.pojo.dto.BookingsUpdateDTO;
import com.sz.admin.bookings.pojo.dto.BookingsListDTO;
import com.sz.admin.bookings.pojo.vo.BookingsVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 预订信息表 服务实现类
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Service
@RequiredArgsConstructor
public class BookingsServiceImpl extends ServiceImpl<BookingsMapper, Bookings> implements BookingsService {
    private final RoomTypesService roomTypesService;
    private final RoomsService roomsService;
    private final RoomTypeInventoryService roomTypeInventoryService;
    private final PaymentService paymentService;
    private final RabbitTemplate rabbitTemplate;
    @Override
    @Transactional
    public Long create(BookingsCreateDTO dto){
        Bookings bookings = BeanCopyUtils.copy(dto, Bookings.class);
        Long bcount = dto.getBookCount();
        // 校验酒店此房型的有效性
        RoomTypes roomTypes = roomTypesService.getById(dto.getRoomTypeId());
        CommonResponseEnum.INVALID.message("酒店信息无效").assertNull(roomTypes);

        // 日期二次校验
        LocalDate startDate = dto.getBookingDate();
        LocalDate endDate = dto.getBookingEnd();
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        CommonResponseEnum.INVALID.message("开始日期不能早于今日").assertTrue(startDate.isBefore(LocalDate.now()));
        CommonResponseEnum.INVALID.message("结束日期不能早于开始日期").assertTrue(!startDate.isBefore(endDate));

        CommonResponseEnum.NOLOGIN.message("未登录").assertFalse(StpUtil.isLogin());
        bookings.setUserId(StpUtil.getLoginIdAsLong());
//        bookings.setUserId(1L);
        // 1.库存扣减
        List<RoomTypeInventoryBookDTO> dtoList = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            RoomTypeInventoryBookDTO rdto = new RoomTypeInventoryBookDTO();
            rdto.setRoomTypeId(roomTypes.getRoomTypeId());
            rdto.setBookCount(bcount);
            rdto.setDate(currentDate);
            dtoList.add(rdto);
            currentDate = currentDate.plusDays(1);
        }
        // 批量库存更新
        List<Boolean> results = roomTypeInventoryService.batchBookRoom(dtoList);
        System.out.println(results);
        CommonResponseEnum.INVALID.message("存在日期不可预定").assertTrue(results.contains(false));

        // 2.订单创建
        bookings.setStatus(BookingStatus.PENDING_CONFIRMATION);
        bookings.setIsReview(Boolean.FALSE);
        save(bookings); // todo 验证id是否填充
        // 3.支付单创建
        Double total = daysBetween*roomTypes.getPrice()*bcount;
        PaymentCreateDTO pdto = new PaymentCreateDTO();
        pdto.setBookingId(bookings.getBookingId());
        pdto.setAmount(total);
        paymentService.create(pdto);
        // 发送延迟消息，延迟时间为10分钟（600000毫秒）
        rabbitTemplate.convertAndSend(
                GlobalConstant.DELAY_EXCHANGE_NAME,
                GlobalConstant.DELAY_ROUTING_KEY,
                bookings.getBookingId(),
                message -> {
                    message.getMessageProperties().setHeader("x-delay", 600000);
                    return message;
                }
        );
        return bookings.getBookingId();
    }

    @Override
    @Transactional
    public void confirm(BookingsUpdateDTO dto){


        QueryWrapper wrapper;
        // 有效性校验——如果是待确认则能确认
        wrapper = QueryWrapper.create()
                .eq(Bookings::getBookingId, dto.getBookingId());
        Bookings bookings = getOne(wrapper);
        CommonResponseEnum.INVALID_ID.assertNull(bookings);
        CommonResponseEnum.VALID_ERROR.message("非待确认状态不可确认")
                          .assertFalse(BookingStatus.PENDING_CONFIRMATION.equals(bookings.getStatus()));

        CommonResponseEnum.NOLOGIN.message("未登录").assertFalse(StpUtil.isLogin());
        String ownerId = StpUtil.getLoginIdAsString();
        HotelOwners hotelOwners = QueryChain.of(HotelOwners.class).eq(HotelOwners::getHotelId, bookings.getHotelId()).one();
        Rooms rooms = QueryChain.of(Rooms.class).eq(Rooms::getHotelId, bookings.getHotelId()).one();
        CommonResponseEnum.INVALID.message("该酒店账号不存在").assertNull(hotelOwners);
        CommonResponseEnum.INVALID.message("该酒店房间不存在").assertNull(rooms);

        // 支付单必须已经结束
        Payment payment = paymentService.getOne(QueryChain.of(Payment.class).eq(Payment::getBookingId,bookings.getBookingId()));
        CommonResponseEnum.VALID_ERROR.message("未支付")
                .assertFalse(PaymentStatus.FINISHED.equals(payment.getPaymentStatus()));
        // 同时更改房间状态（已入住）
        RoomsUpdateDTO roomsUpdateDTO = new RoomsUpdateDTO();
        roomsService.toCheckIn(roomsUpdateDTO);

        bookings.setStatus(BookingStatus.CONFIRMED);
        saveOrUpdate(bookings);
    }

    @Override
    @Transactional
    public void finish(BookingsCancelDTO dto){
        QueryWrapper wrapper;
        // 有效性校验——如果是待确认则能确认
        wrapper = QueryWrapper.create()
                .eq(Bookings::getBookingId, dto.getBookingId());
        Bookings bookings = getOne(wrapper);
        CommonResponseEnum.INVALID.assertNull(bookings);
        CommonResponseEnum.VALID_ERROR.message("当前状态不可结束订单")
                          .assertFalse(BookingStatus.CONFIRMED.equals(bookings.getStatus()));

        CommonResponseEnum.NOLOGIN.message("未登录").assertFalse(StpUtil.isLogin());
        String ownerId = StpUtil.getLoginIdAsString();
        HotelOwners hotelOwners = QueryChain.of(HotelOwners.class).eq(HotelOwners::getHotelId, bookings.getHotelId()).one();
        CommonResponseEnum.INVALID.message("该酒店账号不存在").assertNull(hotelOwners);
        CommonResponseEnum.INVALID_TOKEN.assertFalse(ownerId.equals(hotelOwners.getOwnerId()));

        // 同时更改房间状态（清理中）
        RoomsUpdateDTO roomsUpdateDTO = new RoomsUpdateDTO();
        roomsUpdateDTO.setRoomId(bookings.getRoomId());
        roomsService.toCleaning(roomsUpdateDTO);

        bookings.setStatus(BookingStatus.FINISHED);
        saveOrUpdate(bookings);
    }
    @Override
    @Transactional
    //todo 酒店调用--功能完善
    public void cancel(BookingsCancelDTO dto){

        QueryWrapper wrapper;
        // 有效性校验——如果是待确认则能取消
        wrapper = QueryWrapper.create()
                .eq(Bookings::getBookingId, dto.getBookingId());
        Bookings bookings = getOne(wrapper);
        CommonResponseEnum.INVALID.assertNull(bookings);
        CommonResponseEnum.VALID_ERROR.message("非待确认状态不可取消")
                          .assertFalse(BookingStatus.PENDING_CONFIRMATION.equals(bookings.getStatus()));

        // 支付单未支付 || 已经支付 todo 支付了执行退款。
        Payment payment = paymentService.getOne(QueryChain.of(Payment.class).eq(Payment::getBookingId,bookings.getBookingId()));
//        CommonResponseEnum.VALID_ERROR.message("已支付不能取消")
//                .assertTrue(PaymentStatus.FINISHED.equals(payment.getPaymentStatus()));

        PaymentUpdateDTO pdto = new PaymentUpdateDTO();
        pdto.setPaymentId(payment.getPaymentId());
        pdto.setReason(dto.getReason());
        paymentService.finished(pdto);
//
//        // todo 鉴权校验-酒店管理者id从token中获取
//        Long ownerId = 1L;
//        HotelOwners hotelOwners = QueryChain.of(HotelOwners.class).eq(HotelOwners::getHotelId, bookings.getHotelId()).one();
//        CommonResponseEnum.INVALID.message("该酒店账号不存在").assertNull(hotelOwners);
//        CommonResponseEnum.INVALID_TOKEN.assertFalse(ownerId.equals(hotelOwners.getOwnerId()));

        bookings.setStatus(BookingStatus.CANCELED);
        saveOrUpdate(bookings);
    }

    @Override
    public PageResult<BookingsVO> page(BookingsListDTO dto){
        // 查询用户订单直接传入sa-token对应id
        Page<BookingsVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), BookingsVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<BookingsVO> list(BookingsListDTO dto){
        return listAs(buildQueryWrapper(dto), BookingsVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto){
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    public BookingsVO detail(Object id){
        Bookings bookings = getById((Serializable) id);
        CommonResponseEnum.INVALID_ID.assertNull(bookings);
        return BeanCopyUtils.copy(bookings, BookingsVO.class);
    }

    private static QueryWrapper buildQueryWrapper(BookingsListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(Bookings.class);
        if (Utils.isNotNull(dto.getUserId())) {
            wrapper.eq(Bookings::getUserId, dto.getUserId());
        }
        if (Utils.isNotNull(dto.getHotelId())) {
            wrapper.eq(Bookings::getHotelId, dto.getHotelId());
        }
        if (Utils.isNotNull(dto.getRoomTypeId())) {
            wrapper.eq(Bookings::getRoomTypeId, dto.getRoomTypeId());
        }
        if (Utils.isNotNull(dto.getBookingDateStart()) && Utils.isNotNull(dto.getBookingDateEnd())) {
            wrapper.between(Bookings::getCreatedAt, dto.getBookingDateStart(), dto.getBookingDateEnd());
        }
        if (Utils.isNotNull(dto.getStatus())) {
            wrapper.eq(Bookings::getStatus, dto.getStatus());
        }
        wrapper.orderBy(Bookings::getCreatedAt,false);
        return wrapper;
    }

    // 用户取消支付事件监听
    @EventListener
    @Transactional
    public void handlePaymentCancelled(PaymentCancelledEvent event) {
        Long bookingId = event.getBookingId();
        Bookings booking = getById(bookingId);
        CommonResponseEnum.INVALID_ID.assertNull(booking);
        if(!BookingStatus.CANCELED.equals(booking.getStatus())){
            CommonResponseEnum.VALID_ERROR.message("非待确认状态不可取消")
                    .assertFalse(BookingStatus.PENDING_CONFIRMATION.equals(booking.getStatus()));

            booking.setStatus(BookingStatus.CANCELED);
            saveOrUpdate(booking);
        }
        // 库存释放
        LocalDate startDate = booking.getBookingDate();
        LocalDate endDate = booking.getBookingEnd();
        List<RoomTypeInventoryBookDTO> dtoList = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            RoomTypeInventoryBookDTO rbdto = new RoomTypeInventoryBookDTO();
            rbdto.setRoomTypeId(booking.getRoomTypeId());
            rbdto.setBookCount(booking.getBookCount());
            rbdto.setDate(currentDate);
            dtoList.add(rbdto);
            currentDate = currentDate.plusDays(1);
        }
        List<Boolean> results = roomTypeInventoryService.batchCancelRoom(dtoList);
        CommonResponseEnum.INVALID.message("存在日期无效取消").assertTrue(results.contains(false));

    }
}