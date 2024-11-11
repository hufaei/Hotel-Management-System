package com.sz.admin.users.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * <p>
 * Users导入DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Users导入DTO")
public class UsersImportDTO {

    @ExcelProperty(value = "用户名")
    @Schema(description =  "用户名")
    private String username;

    @ExcelProperty(value = "用户邮箱，用于登录和接收通知")
    @Schema(description =  "用户邮箱，用于登录和接收通知")
    private String email;

    @ExcelProperty(value = "用户手机号码")
    @Schema(description =  "用户手机号码")
    private String phone;

    @ExcelProperty(value = "加密后的密码")
    @Schema(description =  "加密后的密码")
    private String passwordHash;

    @Schema(description =  "注册时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @ExcelProperty(value = "删除标识，0为未删除，1为已删除")
    @Schema(description =  "删除标识，0为未删除，1为已删除")
    private String isDeleted;

}