package com.sz.admin.roomtypes.pojo.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * RoomTypes返回vo
 * </p>
 *
 * @author sz-admin
 * @since 2024-12-01
 */
@Data
@Schema(description = "RoomTypes返回vo")
public class RoomTypesVO {

    @Schema(description =  "房型ID")
    private String roomTypeId;

    @ExcelProperty(value = "关联的酒店ID")
    @Schema(description =  "关联的酒店ID")
    private String hotelId;

    @ExcelProperty(value = "房型类型")
    @Schema(description =  "房型类型")
    private String roomType;

    @ExcelProperty(value = "房型起步价，单位：元")
    @Schema(description ="房型起步价，单位：元")
    private Double price;

    @ExcelProperty(value = "房型照片URL列表，存储为JSON数组")
    @Schema(description =  "房型照片URL列表，存储为JSON数组")
    private String photoUrls;

    @ExcelProperty(value ="房型信息")
    @Schema(description ="房型信息")
    private String info;

    @ExcelProperty(value ="房型信息")
    @Schema(description ="内部设施描述信息")
    private String description;

    @ExcelProperty(value = "房型记录创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "房型记录创建时间")
    private LocalDateTime createdAt;

}