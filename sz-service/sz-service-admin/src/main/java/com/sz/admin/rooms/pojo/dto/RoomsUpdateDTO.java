package com.sz.admin.rooms.pojo.dto;

import com.sz.platform.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

/**
 * <p>
 * Rooms修改DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Rooms修改DTO")
public class RoomsUpdateDTO {

    @Schema(description =  "房间ID")
    private Long roomId;

    @Schema(description =  "关联的酒店ID")
    private Integer hotelId;

    @Schema(description =  "房间号")
    private String roomNumber;

    @Schema(description =  "房间类型")
    private RoomType roomType;

    @Schema(description =  "房间价格，单位为元")
    private BigDecimal price;

    @Schema(description =  "房间大小，单位为平方米")
    private BigDecimal size;

    @Schema(description =  "床类型，例如双人床、单人床")
    private String bedInfo;

    @Schema(description =  "房间最大可入住人数")
    private Integer capacity;

    @Schema(description =  "当前房间库存数量")
    private Integer availability;

}