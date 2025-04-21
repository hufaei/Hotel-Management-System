package com.sz.admin.rooms.pojo.dto;

import com.sz.platform.enums.RoomStatus;
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
    private String hotelId;

    @Schema(description =  "房间号")
    private String roomNumber;

    @Schema(description =  "房间类型")
    private String roomType;

    @Schema(description ="房间状态")
    private RoomStatus roomStatus;

}