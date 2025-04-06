package com.sz.admin.roomtypes.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.sz.core.common.entity.PageQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * <p>
 * RoomTypes查询DTO
 * </p>
 *
 * @author sz-admin
 * @since 2024-12-01
 */
@Data
@Schema(description = "RoomTypes查询DTO")
public class RoomTypesListDTO extends PageQuery {

    @Schema(description =  "关联的酒店ID")
    private String hotelId;

    @Schema(description =  "房型类型")
    private String roomType;

    @Schema(description =  "房型记录创建时间开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAtStart;

    @Schema(description =  "房型记录创建时间结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAtEnd;

}