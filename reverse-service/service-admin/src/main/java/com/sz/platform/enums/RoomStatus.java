package com.sz.platform.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.EnumValue;

@Schema(description = "酒店房间的类型")
public enum RoomStatus {
    READY(0, "空闲中"),
    CLEANING(1, "清理中"),
    CHECK_IN(3, "已入住");


    private final int code;

    private final String desc;  // 描述信息

    RoomStatus(int code, String desc) {
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
    public static String fromCode(int code) {
        for (RoomStatus statu : RoomStatus.values()) {
            if (statu.code == code) {
                return statu.desc;
            }
        }
        throw new IllegalArgumentException("Invalid roomStatus code: " + code);
    }
}
