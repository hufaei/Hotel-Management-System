package com.sz.admin.roomtypeinventory.pojo.po;

import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.io.Serial;
import com.sz.mysql.EntityChangeListener;
import java.time.LocalDate;

/**
* <p>
* 房型预订存量
* </p>
*
* @author botsuchi
* @since 2024-12-01
*/
@Data
@Table(value = "room_type_inventory", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "房型预订存量")
public class RoomTypeInventory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Schema(description ="关联的房型ID")
    private String roomTypeId;

    @Id
    @Schema(description ="日期")
    private LocalDate date;

    @Schema(description ="可用房间数量")
    private Long availableQuantity;

    @Schema(description ="设定当日库存")
    private Long preset;

}