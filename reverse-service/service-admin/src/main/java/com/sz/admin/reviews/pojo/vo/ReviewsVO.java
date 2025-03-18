package com.sz.admin.reviews.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * Reviews返回vo
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Reviews返回vo")
public class ReviewsVO {

    @ExcelIgnore
    @Schema(description =  "评价ID")
    private Long reviewId;

    @ExcelProperty(value = "关联的预订ID")
    @Schema(description =  "关联的预订ID")
    private Long bookingId;

    @ExcelProperty(value = "评价用户ID")
    @Schema(description =  "评价用户ID")
    private Long userId;

    @ExcelProperty(value = "评分，范围为1到5")
    @Schema(description =  "评分，范围为1到5")
    private Integer rating;

    @ExcelProperty(value = "用户评价内容")
    @Schema(description =  "用户评价内容")
    private String comment;

    @ExcelProperty(value = "评价创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "评价创建时间")
    private LocalDateTime createdAt;

}