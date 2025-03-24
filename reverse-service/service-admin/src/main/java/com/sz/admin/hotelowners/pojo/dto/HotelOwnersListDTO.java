package com.sz.admin.hotelowners.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.sz.core.common.entity.PageQuery;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * <p>
 * HotelOwners查询DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "HotelOwners查询DTO")
public class HotelOwnersListDTO extends PageQuery {

    @Schema(description =  "关联的酒店ID")
    private String hotelId;

    @Schema(description =  "代表人姓名")
    private String name;

    @Schema(description =  "代表人邮箱")
    private String email;

    @Schema(description =  "代表人联系电话")
    private String phone;


}