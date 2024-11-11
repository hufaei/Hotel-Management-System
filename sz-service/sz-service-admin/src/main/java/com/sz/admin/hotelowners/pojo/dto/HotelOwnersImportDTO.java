package com.sz.admin.hotelowners.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * <p>
 * HotelOwners导入DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "HotelOwners导入DTO")
public class HotelOwnersImportDTO {

    @ExcelProperty(value = "关联的酒店ID")
    @Schema(description =  "关联的酒店ID")
    private Long hotelId;

    @ExcelProperty(value = "代表人姓名")
    @Schema(description =  "代表人姓名")
    private String name;

    @ExcelProperty(value = "代表人邮箱")
    @Schema(description =  "代表人邮箱")
    private String email;

    @ExcelProperty(value = "代表人联系电话")
    @Schema(description =  "代表人联系电话")
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