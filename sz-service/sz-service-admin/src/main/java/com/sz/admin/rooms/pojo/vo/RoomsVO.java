package com.sz.admin.rooms.pojo.vo;

import com.sz.platform.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

/**
 * <p>
 * Rooms返回vo
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Rooms返回vo")
public class RoomsVO {

    @ExcelIgnore
    @Schema(description =  "房间ID")
    private Long roomId;

    @ExcelProperty(value = "关联的酒店ID")
    @Schema(description =  "关联的酒店ID")
    private Integer hotelId;

    @ExcelProperty(value = "房间号")
    @Schema(description =  "房间号")
    private String roomNumber;

    @ExcelProperty(value = "房间类型")
    @Schema(description =  "房间类型")
    private String roomType;

    @ExcelProperty(value = "房间价格，单位为元")
    @Schema(description =  "房间价格，单位为元")
    private Long price;

    @ExcelProperty(value = "房间大小，单位为平方米")
    @Schema(description =  "房间大小，单位为平方米")
    private Long size;

    @ExcelProperty(value = "床类型，例如双人床、单人床")
    @Schema(description =  "床类型，例如双人床、单人床")
    private String bedInfo;

    @ExcelProperty(value = "房间最大可入住人数")
    @Schema(description =  "房间最大可入住人数")
    private Integer capacity;

    @ExcelProperty(value = "当前房间库存数量")
    @Schema(description =  "当前房间库存数量")
    private Integer availability;

}