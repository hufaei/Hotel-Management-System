package com.sz.admin.bookings.pojo.po;

import com.mybatisflex.annotation.*;
import com.sz.platform.enums.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.io.Serial;
import com.sz.mysql.EntityChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* <p>
* 预订信息表
* </p>
*
* @author botsuchi
* @since 2024-10-27
*/
@Data
@Table(value = "bookings", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "预订信息表")
public class Bookings implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description ="预订ID")
    private Long bookingId;

    @Schema(description ="关联的用户ID")
    private Integer userId;

    @Schema(description ="关联的酒店ID")
    private Integer hotelId;

    @Schema(description ="关联的房间ID")
    private Integer roomId;

    @Schema(description ="预订用户的手机号码")
    private String userPhone;

    @Schema(description ="预订日期")
    private LocalDate bookingDate;

    @Schema(description ="预订结束日期")
    private LocalDate bookingEnd;

    @Schema(description ="预订状态")
    private BookingStatus status;

    @Schema(description ="预订创建时间")
    private LocalDateTime createdAt;

}