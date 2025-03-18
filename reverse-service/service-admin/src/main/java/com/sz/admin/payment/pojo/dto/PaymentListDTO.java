package com.sz.admin.payment.pojo.dto;

import com.sz.platform.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.sz.core.common.entity.PageQuery;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * <p>
 * Payment查询DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-11-23
 */
@Data
@Schema(description = "Payment查询DTO")
public class PaymentListDTO extends PageQuery {

    @Schema(description =  "支付单ID")
    private Long paymentId;

    @Schema(description =  "关联的预订订单ID，支付完成后可关联")
    private Long bookingId;

    @Schema(description =  "支付状态")
    private PaymentStatus paymentStatus;

    @Schema(description =  "支付创建时间开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAtStart;

    @Schema(description =  "支付创建时间结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAtEnd;

}