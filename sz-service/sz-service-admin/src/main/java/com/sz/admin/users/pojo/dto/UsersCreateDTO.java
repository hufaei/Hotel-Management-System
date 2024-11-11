package com.sz.admin.users.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * Users添加DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Users添加DTO")
public class UsersCreateDTO {

   @Schema(description =  "用户名")
   private String username;

   @Schema(description =  "用户邮箱，用于登录和接收通知")
   private String email;

   @Schema(description =  "用户手机号码")
   private String phone;

   @Schema(description =  "设置的密码")
   private String password;

   @Schema(description =  "注册时间")
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private LocalDateTime createdAt;

}