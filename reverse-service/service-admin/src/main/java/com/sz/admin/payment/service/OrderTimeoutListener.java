package com.sz.admin.payment.service;

import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.sz.admin.bookings.pojo.po.Bookings;
import com.sz.admin.bookings.service.BookingsService;
import com.sz.admin.payment.pojo.dto.PaymentUpdateDTO;
import com.sz.admin.payment.pojo.po.Payment;
import com.sz.admin.payment.pojo.vo.PaymentVO;
import com.sz.core.common.constant.GlobalConstant;
import com.sz.core.common.enums.CommonResponseEnum;
import com.sz.platform.enums.BookingStatus;
import com.sz.platform.enums.PaymentStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTimeoutListener {
    @Autowired
    private BookingsService bookingsService;
    @Autowired
    private PaymentService paymentService;

    @RabbitListener(queues = GlobalConstant.DELAY_QUEUE_NAME)
    @Transactional
    public void handleOrderTimeout(String bookingId) {
        // 处理订单超时逻辑，例如取消订单
        System.out.println("订单超时未支付，订单ID：" + bookingId);
        // 取消订单逻辑...
        QueryWrapper wrapper = QueryWrapper.create().from(Bookings.class);
        wrapper.eq(Bookings::getBookingId, bookingId);
        Bookings bookings = bookingsService.getOne(wrapper);
        CommonResponseEnum.INVALID.assertNull(bookings);
        PaymentVO payment = paymentService.detailByBookingId(bookingId);
        if(PaymentStatus.UNPAID.equals(payment.getPaymentStatus())){
            PaymentUpdateDTO updateDTO = new PaymentUpdateDTO();
            updateDTO.setPaymentId(payment.getPaymentId());
            updateDTO.setReason("订单超时未支付，自动取消");
            paymentService.cancel(updateDTO);
        }
    }
}
