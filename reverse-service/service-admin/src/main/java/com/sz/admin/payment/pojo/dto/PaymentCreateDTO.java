package com.sz.admin.payment.pojo.dto;

import com.sz.platform.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * Payment添加DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-11-23
 */
@Data
@Schema(description = "Payment添加DTO")
public class PaymentCreateDTO {

   @Schema(description =  "关联的预订订单ID，支付完成后可关联")
   private Long bookingId;

   @Schema(description =  "支付金额，单位为元")
   private Double amount;

}