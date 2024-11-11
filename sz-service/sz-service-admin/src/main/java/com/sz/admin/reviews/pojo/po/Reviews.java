package com.sz.admin.reviews.pojo.po;

import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.io.Serial;
import com.sz.mysql.EntityChangeListener;
import java.time.LocalDateTime;

/**
* <p>
* 用户评价表
* </p>
*
* @author botsuchi
* @since 2024-10-27
*/
@Data
@Table(value = "reviews", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "用户评价表")
public class Reviews implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description ="评价ID")
    private Long reviewId;

    @Schema(description ="关联的预订ID")
    private Long bookingId;

    @Schema(description ="评价用户ID")
    private Long userId;

    @Schema(description ="评分，范围为1到5")
    private Integer rating;

    @Schema(description ="用户评价内容")
    private String comment;

    @Schema(description ="评价创建时间")
    private LocalDateTime createdAt;

}