package com.sz.admin.payment.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.sz.platform.enums.PaymentStatus;

/**
 * <p>
 * Payment返回vo
 * </p>
 *
 * @author botsuchi
 * @since 2024-11-23
 */
@Data
@Schema(description = "Payment返回vo")
public class PaymentVO {

    @Schema(description =  "支付单ID")
    private Long paymentId;

    @Schema(description =  "关联的预订订单ID，支付完成后可关联")
    private Long bookingId;

    @Schema(description =  "支付金额，单位为元")
    private Long amount;

    @Schema(description =  "支付状态")
    private PaymentStatus paymentStatus;

    @Schema(description =  "支付取消或退款原因")
    private String reason;

    @Schema(description =  "支付创建时间")
    private LocalDateTime createdAt;

}