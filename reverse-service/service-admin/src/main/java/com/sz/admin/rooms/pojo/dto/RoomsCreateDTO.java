package com.sz.admin.rooms.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

/**
 * <p>
 * Rooms添加DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Rooms添加DTO")
public class RoomsCreateDTO {

   @Schema(description =  "关联的酒店ID")
   private String hotelId;

   @Schema(description =  "房间号")
   private String roomNumber;

   @Schema(description =  "房间类型")
   private String roomType;

//   @Schema(description ="房间状态")
//   private RoomStatus roomStatus;

//   @Schema(description =  "当前房间库存数量")
//   private Integer availability;

}