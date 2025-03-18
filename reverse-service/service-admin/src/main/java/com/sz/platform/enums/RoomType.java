package com.sz.platform.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.EnumValue;

@Schema(description = "酒店房间的类型")
public enum RoomType {
    SINGLE_BED(0, "单床"),
    DOUBLE_BED(1, "大床"),
    TWIN_BED(2, "双床"),
    SPECIAL(3, "特价");


    private final int code;

    private final String desc;  // 描述信息

    RoomType(int code, String desc) {
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
        for (RoomType type : RoomType.values()) {
            if (type.code == code) {
                return type.desc;
            }
        }
        throw new IllegalArgumentException("Invalid room type code: " + code);
    }
}
