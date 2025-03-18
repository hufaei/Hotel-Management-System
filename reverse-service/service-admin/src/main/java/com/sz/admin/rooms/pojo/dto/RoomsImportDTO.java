package com.sz.admin.rooms.pojo.dto;

import com.sz.platform.enums.RoomStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
/**
 * <p>
 * Rooms导入DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Rooms导入DTO")
public class RoomsImportDTO {

    @ExcelProperty(value = "关联的酒店ID")
    @Schema(description =  "关联的酒店ID")
    private Long hotelId;

    @ExcelProperty(value = "房间号")
    @Schema(description =  "房间号")
    private String roomNumber;

    @ExcelProperty(value = "房间类型")
    @Schema(description =  "房间类型")
    private String roomType;

    @ExcelProperty(value = "房间价格，单位为元")
    @Schema(description =  "房间价格，单位为元")
    private BigDecimal price;

    @ExcelProperty(value = "房间大小，单位为平方米")
    @Schema(description =  "房间大小，单位为平方米")
    private BigDecimal size;

    @ExcelProperty(value = "床类型，例如双人床、单人床")
    @Schema(description =  "床类型，例如双人床、单人床")
    private String bedInfo;

    @ExcelProperty(value = "房间最大可入住人数")
    @Schema(description =  "房间最大可入住人数")
    private Integer capacity;

    @ExcelProperty(value = "房间状态")
    @Schema(description ="房间状态")
    private RoomStatus roomStatus;

//    @ExcelProperty(value = "当前房间库存数量")
//    @Schema(description =  "当前房间库存数量")
//    private Integer availability;

}