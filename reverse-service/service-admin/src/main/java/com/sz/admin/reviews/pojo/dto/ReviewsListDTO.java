package com.sz.admin.reviews.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.sz.core.common.entity.PageQuery;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * <p>
 * Reviews查询DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Reviews查询DTO")
public class ReviewsListDTO extends PageQuery {

    @Schema(description =  "关联的预订ID")
    private String HotelId;

    @Schema(description =  "评价用户ID")
    private Long userId;

    @Schema(description ="总评分上限")
    private Double ratingCeil;

    @Schema(description ="总评分下限")
    private Double ratingFloor;

    @Schema(description =  "评价创建时间开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAtStart;

    @Schema(description =  "评价创建时间结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAtEnd;

}