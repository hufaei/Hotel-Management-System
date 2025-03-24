package com.sz.admin.roomtypeinventory.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

/**
 * <p>
 * RoomTypeInventory返回vo
 * </p>
 *
 * @author botsuchi
 * @since 2024-12-01
 */
@Data
@Schema(description = "RoomTypeInventory返回vo")
public class RoomTypeInventoryVO {

    @Schema(description =  "关联的房型ID")
    private String roomTypeId;

    @Schema(description =  "日期")
    private LocalDate date;

    @Schema(description =  "可用房间数量")
    private Long availableQuantity;

    @Schema(description ="设定当日库存")
    private Long preset;
}