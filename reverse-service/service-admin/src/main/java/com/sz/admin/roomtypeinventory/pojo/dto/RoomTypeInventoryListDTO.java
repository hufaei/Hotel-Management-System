package com.sz.admin.roomtypeinventory.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.sz.core.common.entity.PageQuery;
import java.time.LocalDate;
/**
 * <p>
 * RoomTypeInventory查询DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-12-01
 */
@Data
@Schema(description = "RoomTypeInventory查询DTO")
public class RoomTypeInventoryListDTO extends PageQuery {

    @Schema(description =  "关联的房型ID")
    private Long roomTypeId;

    @Schema(description =  "日期")
    private LocalDate date;

}