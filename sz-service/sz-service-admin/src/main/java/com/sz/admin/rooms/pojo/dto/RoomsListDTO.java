package com.sz.admin.rooms.pojo.dto;

import com.sz.platform.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.sz.core.common.entity.PageQuery;
import java.math.BigDecimal;
/**
 * <p>
 * Rooms查询DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Rooms查询DTO")
public class RoomsListDTO extends PageQuery {

    @Schema(description =  "关联的酒店ID")
    private Integer hotelId;

    @Schema(description =  "房间号")
    private String roomNumber;

    @Schema(description =  "房间类型")
    private Integer roomType;

    @Schema(description =  "房间价格(查小于)，单位为元")
    private Long price;

    @Schema(description =  "房间大小，单位为平方米")
    private Long size;

    @Schema(description =  "床类型，例如双人床、单人床")
    private String bedInfo;

    @Schema(description =  "房间最大可入住人数")
    private Integer capacity;

    @Schema(description =  "当前房间库存数量")
    private Integer availability;

}