package com.sz.admin.roomtypes.pojo.dto;

import com.sz.platform.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

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
   private Long hotelId;

   @Schema(description =  "房型类型")
   private RoomType roomType;

   @Schema(description =  "房型价格，单位为元")
   private Long price;

   @Schema(description ="入住人数")
   private Long capacity;

   @Schema(description =  "房型照片URL列表，存储为JSON数组")
   private String photoUrls;

   @Schema(description =  "房型大小，单位为平方米")
   private Long size;

   @Schema(description =  "房型描述信息")
   private String description;

}