package com.sz.admin.admins.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * Admins返回vo
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Admins返回vo")
public class AdminsVO {

    @ExcelProperty(value = "管理员ID")
    @Schema(description =  "管理员ID")
    private Long adminId;

    @ExcelProperty(value = "管理员用户名")
    @Schema(description =  "管理员用户名")
    private String username;

    @ExcelProperty(value = "加密后的管理员密码")
    @Schema(description =  "加密后的管理员密码")
    private String passwordHash;

    @ExcelProperty(value = "管理员邮箱，用于接收系统通知")
    @Schema(description =  "管理员邮箱，用于接收系统通知")
    private String email;

    @ExcelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "创建时间")
    private LocalDateTime createdAt;

}