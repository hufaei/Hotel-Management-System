package com.sz.admin.hotels.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * Hotels添加DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Hotels添加DTO")
public class HotelsCreateDTO {

   @Schema(description =  "酒店名称")
   private String hotelName;

   @Schema(description =  "酒店地址")
   private String address;

   @Schema(description =  "酒店纬度信息")
   private BigDecimal latitude;

   @Schema(description =  "酒店经度信息")
   private BigDecimal longitude;

   @Schema(description =  "酒店联系邮箱")
   private String contactEmail;

   @Schema(description =  "酒店联系电话")
   private String contactPhone;

}