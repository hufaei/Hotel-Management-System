package com.sz.admin.bookings.pojo.dto;

import com.sz.platform.enums.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.sz.core.common.entity.PageQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * <p>
 * Bookings查询DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Bookings查询DTO")
public class BookingsListDTO extends PageQuery {

    @Schema(description =  "关联的用户ID")
    private Long userId;

    @Schema(description ="预订ID")
    private Long bookingId;

    @Schema(description =  "关联的酒店ID")
    private String hotelId;

    @Schema(description ="房型ID")
    private String roomTypeId;

    @Schema(description ="预订数量")
    private Long bookCount;

    @Schema(description =  "订单创建日期开始")
    private LocalDate bookingDateStart;

    @Schema(description =  "订单创建日期结束")
    private LocalDate bookingDateEnd;

    @Schema(description =  "预订状态")
    private BookingStatus status;

    @Schema(description ="是否评价")
    private Boolean isReview;

}