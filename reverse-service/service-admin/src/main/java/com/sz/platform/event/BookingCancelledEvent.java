package com.sz.platform.event;

import com.sz.core.common.event.BaseEvent;

public class BookingCancelledEvent extends BaseEvent<Long> {

    public BookingCancelledEvent(Object source, Long bookingId) {
        super(source, bookingId);
    }

    public Long getBookingId() {
        return getPayload(); // 从 payload 中获取 bookingId
    }
}
