package com.sz.admin.bookings.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * <p>
 * Bookings导入DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Bookings导入DTO")
public class BookingsImportDTO {

    @ExcelProperty(value = "关联的用户ID")
    @Schema(description =  "关联的用户ID")
    private Integer userId;

    @ExcelProperty(value = "关联的酒店ID")
    @Schema(description =  "关联的酒店ID")
    private Integer hotelId;

    @ExcelProperty(value = "关联的房间ID")
    @Schema(description =  "关联的房间ID")
    private Integer roomId;

    @ExcelProperty(value = "预订用户的手机号码")
    @Schema(description =  "预订用户的手机号码")
    private String userPhone;

    @ExcelProperty(value = "预订日期")
    @Schema(description =  "预订日期")
    private LocalDate bookingDate;

    @ExcelProperty(value = "预订状态")
    @Schema(description =  "预订状态")
    private String status;

    @Schema(description =  "预订创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}