package com.sz.admin.hotels.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.sz.core.common.entity.PageQuery;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Schema(description ="所属省份")
    private String province;

    @Schema(description ="所属市区")
    private String city;

    @Schema(description ="所属县")
    private String country;

    @Schema(description ="酒店星级")
    private Integer stars;

    @Schema(description =  "酒店地址")
    private String address;

    @Schema(description =  "开业年限开始")
    private LocalDate openingDateStart;

    @Schema(description =  "开业年限结束")
    private LocalDate openingDateEnd;

    @Schema(description = "起价")
    private BigDecimal minPrice;
    @Schema(description = "外显评分")
    private Double rate;

}