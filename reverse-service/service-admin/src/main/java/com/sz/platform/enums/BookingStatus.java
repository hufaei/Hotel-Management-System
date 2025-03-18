package com.sz.platform.enums;

import com.mybatisflex.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "酒店预订的状态")
public enum BookingStatus {
    PENDING_CONFIRMATION(1, "待确认"),
    CONFIRMED(2, "已确认"),

    CANCELED(3, "已取消"),
    FINISHED(4,"已结束");


    private final int code;

    private final String desc;

    BookingStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
    @EnumValue
    public String getDesc() {
        return desc;
    }

    public static BookingStatus fromCode(int code) {
        for (BookingStatus status : BookingStatus.values()) {
            if (status.getCode()==code){
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid booking code: " + code);
    }
    public static BookingStatus fromEnum(String name) {
        for (BookingStatus status : BookingStatus.values()) {
            if (status.name().equals(name)){
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid booking enum name: " + name);
    }
}
