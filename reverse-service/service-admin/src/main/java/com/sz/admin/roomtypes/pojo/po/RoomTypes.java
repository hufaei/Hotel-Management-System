package com.sz.admin.roomtypes.pojo.po;

import com.mybatisflex.annotation.*;
import com.sz.platform.enums.RoomType;
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

    @Id(keyType = KeyType.Auto)
    @Schema(description ="房型ID")
    private Long roomTypeId;

    @Schema(description ="关联的酒店ID")
    private Long hotelId;

    @Schema(description ="房型类型")
    private RoomType roomType;

    @Schema(description ="入住人数")
    private Long capacity;

    @Schema(description ="房型价格，单位为元")
    private Long price;

    @Schema(description ="房型照片URL列表，存储为JSON数组")
    private String photoUrls;

    @Schema(description ="房型大小，单位为平方米")
    private Long size;

    @Schema(description ="房型描述信息")
    private String description;

    @Schema(description ="房型记录创建时间")
    private LocalDateTime createdAt;

}