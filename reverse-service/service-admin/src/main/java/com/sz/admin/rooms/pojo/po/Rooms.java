package com.sz.admin.rooms.pojo.po;

import com.mybatisflex.annotation.*;
import com.sz.platform.enums.RoomStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.io.Serial;
import com.sz.mysql.EntityChangeListener;
import java.math.BigDecimal;

/**
* <p>
* 房间信息表
* </p>
*
* @author botsuchi
* @since 2024-10-27
*/
@Data
@Table(value = "rooms", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "房间信息表")
public class Rooms implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description ="房间ID")
    private Long roomId;

    @Schema(description ="关联的酒店ID")
    private Long hotelId;

    @Schema(description ="房间号")
    private String roomNumber;

    @Schema(description ="房间类型")
    private String roomType;

    @Schema(description ="房间状态")
    private RoomStatus roomStatus;

}