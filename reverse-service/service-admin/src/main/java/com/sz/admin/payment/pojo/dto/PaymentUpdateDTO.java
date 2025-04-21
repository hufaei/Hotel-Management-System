package com.sz.admin.payment.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * Payment修改DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-11-23
 */
@Data
@Schema(description = "Payment修改DTO")
public class PaymentUpdateDTO {

    @Schema(description =  "支付单ID")
    private Long paymentId;

    @Schema(description =  "支付退款原因")
    private String reason;

}