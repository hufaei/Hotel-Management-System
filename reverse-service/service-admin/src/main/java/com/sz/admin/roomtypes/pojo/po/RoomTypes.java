package com.sz.admin.roomtypes.pojo.po;

import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.io.Serial;
import com.sz.mysql.EntityChangeListener;
import java.time.LocalDateTime;

/**
* <p>
* 房型表
* </p>
*
* @author sz-admin
* @since 2024-12-01
*/
@Data
@Table(value = "room_types", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "房型表")
public class RoomTypes implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id()
    @Schema(description ="房型ID")
    private String roomTypeId;

    @Schema(description ="关联的酒店ID")
    private Long hotelId;

    @Schema(description ="房型类型")
    private String roomType;

    @Schema(description ="房型起步价，单位：元")
    private Long price;

    @Schema(description ="房型照片URL列表，存储为JSON数组")
    private String photoUrls;

    @Schema(description ="房型信息")
    private String info;

    @Schema(description ="内部设施描述信息")
    private String description;

    @Schema(description ="房型记录创建时间")
    private LocalDateTime createdAt;

}