package com.sz.admin.roomtypes.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * RoomTypes修改DTO
 * </p>
 *
 * @author sz-admin
 * @since 2024-12-01
 */
@Data
@Schema(description = "RoomTypes修改DTO")
public class RoomTypesUpdateDTO {

    @Schema(description =  "房型ID")
    private String roomTypeId;

    @Schema(description =  "房型价格，单位为元")
    private Double price;

    @Schema(description =  "房型照片URL列表，存储为JSON数组")
    private String photoUrls;

    @Schema(description ="房型信息")
    private String info;

    @Schema(description ="内部设施描述信息")
    private String description;

}