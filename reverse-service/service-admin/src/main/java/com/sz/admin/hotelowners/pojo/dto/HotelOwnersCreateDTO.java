package com.sz.admin.hotelowners.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * HotelOwners添加DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "HotelOwners添加DTO")
public class HotelOwnersCreateDTO {

   @Schema(description =  "代表人姓名")
   private String name;

   @Schema(description =  "代表人邮箱")
   private String email;

   @Schema(description =  "代表人联系电话")
   private String phone;

   @Schema(description =  "设置的密码")
   private String password;
//
//   @Schema(description =  "注册时间")
//   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//   private LocalDateTime createdAt;

}