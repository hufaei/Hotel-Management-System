package com.sz.admin.roomtypeinventory.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

/**
 * <p>
 * RoomTypeInventory预订相关DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-12-01
 */
@Data
@Schema(description = "RoomTypeInventory预订相关DTO")
public class RoomTypeInventoryBookDTO {

    @Schema(description =  "关联的房型ID")
    private String roomTypeId;

    @Schema(description =  "日期")
    private LocalDate date;

    @Schema(description =  "订购数量")
    private Long bookCount;


}