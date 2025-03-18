package com.sz.admin.bookings.pojo.dto;

import com.sz.platform.enums.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * Bookings添加DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Bookings添加DTO")
public class BookingsCreateDTO {

   @Schema(description =  "关联的酒店ID")
   private Long hotelId;

   @Schema(description ="房型ID")
   private Long roomTypeId;

   @Schema(description ="预留用户的手机号码")
   private String userPhone;

   @Schema(description =  "预订日期")
   private LocalDate bookingDate;

   @Schema(description ="预订结束日期")
   private LocalDate bookingEnd;

   @Schema(description ="预订房间数量")
   private Long bookCount;


}