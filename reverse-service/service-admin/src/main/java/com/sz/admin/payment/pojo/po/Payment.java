package com.sz.admin.payment.pojo.po;

import com.mybatisflex.annotation.*;
import com.sz.platform.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.io.Serial;
import com.sz.mysql.EntityChangeListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* <p>
* 支付单表
* </p>
*
* @author botsuchi
* @since 2024-11-23
*/
@Data
@Table(value = "payment", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "支付单表")
public class Payment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description ="支付单ID")
    private Long paymentId;

    @Schema(description ="关联的预订订单ID，支付完成后可关联")
    private Long bookingId;

    @Schema(description ="支付金额，单位为元")
    private Long amount;

    @Schema(description ="支付状态")
    private PaymentStatus paymentStatus;

    @Schema(description ="支付取消或退款原因")
    private String reason;

    @Schema(description ="支付创建时间")
    private LocalDateTime createdAt;

    @Column(isLogicDelete = true)
    @Schema(description ="删除标识，0为未删除，1为已删除")
    private String isDeleted;
}