package com.sz.admin.rooms.pojo.dto;

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

}