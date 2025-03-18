package com.sz.platform.event;

import com.sz.core.common.event.BaseEvent;

public class PaymentCancelledEvent extends BaseEvent<Long> {

    public PaymentCancelledEvent(Object source, Long bookingId) {
        super(source, bookingId);
    }

    public Long getBookingId() {
        return getPayload(); // 从 payload 中获取 bookingId
    }
}
