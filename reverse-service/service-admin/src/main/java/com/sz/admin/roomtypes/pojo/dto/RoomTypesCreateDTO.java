package com.sz.admin.roomtypes.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * <p>
 * RoomTypes添加DTO
 * </p>
 *
 * @author sz-admin
 * @since 2024-12-01
 */
@Data
@Schema(description = "RoomTypes添加DTO")
public class RoomTypesCreateDTO {

   @Schema(description =  "关联的酒店ID")
   private String hotelId;

   @Schema(description =  "房型ID")
   private String roomTypeId;

   @Schema(description =  "房型类型")
   private String roomType;

   @Schema(description =  "房型起步价，单位为元")
   private Double price;

   @Schema(description =  "房型照片URL列表，存储为JSON数组")
   private String photoUrls;

   @Schema(description ="房型信息")
   private String info;

   @Schema(description ="内部设施描述信息")
   private String description;

}