package com.sz.platform.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.EnumValue;

@Schema(description = "支付状态的枚举类")
public enum PaymentStatus {
    UNPAID(0, "未支付"),
    CANCEL(1, "已取消"),
    FINISHED(2,"已结束");

    private final int code;       // 状态码
    private final String desc;    // 描述信息

    PaymentStatus(int code, String desc) {
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

    /**
     * 根据状态码获取描述信息
     *
     * @param code 状态码
     * @return 状态描述
     */
    public static String fromCode(int code) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.code == code) {
                return status.desc;
            }
        }
        throw new IllegalArgumentException("Invalid paymentStatus code: " + code);
    }
}
