package com.sz.admin.admins.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * Admins添加DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Admins添加DTO")
public class AdminsCreateDTO {

   @Schema(description =  "管理员用户名")
   private String username;

   @Schema(description =  "加密后的管理员密码")
   private String passwordHash;

   @Schema(description =  "管理员邮箱，用于接收系统通知")
   private String email;

   @Schema(description =  "创建时间")
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private LocalDateTime createdAt;

}