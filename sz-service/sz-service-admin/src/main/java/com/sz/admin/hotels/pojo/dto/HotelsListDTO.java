package com.sz.admin.hotels.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.sz.core.common.entity.PageQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * <p>
 * Hotels查询DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Hotels查询DTO")
public class HotelsListDTO extends PageQuery {

    @Schema(description =  "酒店ID")
    private Long hotelId;

    @Schema(description =  "酒店名称")
    private String hotelName;

    @Schema(description =  "酒店地址")
    private String address;

    @Schema(description =  "酒店纬度信息")
    private BigDecimal latitude;

    @Schema(description =  "酒店经度信息")
    private BigDecimal longitude;

}