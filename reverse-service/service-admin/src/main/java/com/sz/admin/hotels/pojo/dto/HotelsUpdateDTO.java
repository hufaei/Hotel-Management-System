package com.sz.admin.hotels.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * Hotels修改DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Hotels修改DTO")
public class HotelsUpdateDTO {

    @Schema(description =  "酒店ID")
    private Long hotelId;

    @Schema(description =  "酒店名称")
    private String hotelName;

    @Schema(description ="酒店英文名称")
    private String englishName;

    @Schema(description ="房间数量")
    private Integer roomCount;

    @Schema(description ="酒店联系电话")
    private String contactPhone;

    @Schema(description ="酒店地址")
    private String address;

    @Schema(description ="酒店描述")
    private String description;

    @Schema(description ="交通描述")
    private String traffic;

    @Schema(description ="房型列表")
    private String roomTypeList;
}