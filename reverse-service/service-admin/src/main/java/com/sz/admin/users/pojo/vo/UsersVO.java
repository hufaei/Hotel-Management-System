package com.sz.admin.users.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * Users返回vo
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Users返回vo")
public class UsersVO {

    @ExcelIgnore
    @Schema(description =  "用户ID")
    private Long userId;

    @ExcelProperty(value = "用户名")
    @Schema(description =  "用户名")
    private String username;

    @ExcelProperty(value = "用户邮箱，用于登录和接收通知")
    @Schema(description =  "用户邮箱，用于登录和接收通知")
    private String email;

    @ExcelProperty(value = "用户手机号码")
    @Schema(description =  "用户手机号码")
    private String phone;

    @ExcelProperty(value = "加密密码")
    @Schema(description ="加密后的密码")
    private String passwordHash;

    @ExcelProperty(value = "注册时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "注册时间")
    private LocalDateTime createdAt;

    @Schema(description =  "余额")
    private BigDecimal balance;

}