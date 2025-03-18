package com.sz.admin.roomtypeinventory.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

/**
 * <p>
 * RoomTypeInventory修改DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-12-01
 */
@Data
@Schema(description = "RoomTypeInventory修改DTO")
public class RoomTypeInventoryUpdateDTO {

    @Schema(description =  "关联的房型ID")
    private Long roomTypeId;

    @Schema(description =  "日期")
    private LocalDate date;

    @Schema(description ="设定当日库存")
    private Long preset;
}