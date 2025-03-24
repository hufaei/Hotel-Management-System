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

    @ExcelProperty(value = "总评分")
    @Schema(description ="总评分")
    private Double rating;

    @ExcelProperty(value ="卫生评分")
    @Schema(description ="卫生评分")
    private Double healthRate;

    @ExcelProperty(value ="环境评分")
    @Schema(description ="环境评分")
    private Double envRate;

    @ExcelProperty(value ="服务评分")
    @Schema(description ="服务评分")
    private Double serviceRate;

    @ExcelProperty(value ="设施评分")
    @Schema(description ="设施评分")
    private Double facilitiesRate;

    @ExcelProperty(value = "用户评价内容")
    @Schema(description =  "用户评价内容")
    private String comment;

    @ExcelProperty(value = "评价创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "评价创建时间")
    private LocalDateTime createdAt;

}