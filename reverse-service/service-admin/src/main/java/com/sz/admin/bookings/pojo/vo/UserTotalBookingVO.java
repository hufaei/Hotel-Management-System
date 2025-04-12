package com.sz.admin.bookings.pojo.vo;

import com.sz.platform.enums.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * Bookings返回vo
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "用户总览视图")
public class UserTotalBookingVO {

    @Schema(description =  "关联的用户ID")
    private Long userId;

    @Schema(description =  "关联的用户ID")
    private String userName;

    @Schema(description =  "总评论数")
    private Long count;

    @Schema(description =  "预订创建时间")
    private String roomType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "预订创建时间")
    private LocalDateTime createdAt;


}