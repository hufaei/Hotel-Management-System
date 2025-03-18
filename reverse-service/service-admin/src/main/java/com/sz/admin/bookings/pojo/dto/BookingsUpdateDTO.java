package com.sz.admin.bookings.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * Bookings修改DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Bookings修改DTO")
public class BookingsUpdateDTO {

    @Schema(description =  "预订ID")
    private Long bookingId;

    @Schema(description ="关联的房间ID")
    private Long roomId;



}