package com.sz.admin.rooms.pojo.vo;

import com.sz.platform.enums.RoomStatus;
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
    private String hotelId;

    @ExcelProperty(value = "房间号")
    @Schema(description =  "房间号")
    private String roomNumber;

    @ExcelProperty(value = "房间类型")
    @Schema(description =  "房间类型")
    private String roomType;


    @ExcelProperty(value = "房间状态")
    @Schema(description ="房间状态")
    private RoomStatus roomStatus;
}