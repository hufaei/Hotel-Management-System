package com.sz.admin.reviews.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * Reviews添加DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Reviews添加DTO")
public class ReviewsCreateDTO {

   @Schema(description =  "关联的预订ID")
   private Long bookingId;

   @Schema(description =  "评分，范围为1到5")
   private Integer rating;

   @Schema(description =  "用户评价内容")
   private String comment;


}