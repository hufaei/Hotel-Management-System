package com.sz.admin.hotelowners.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * HotelOwners修改DTO
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "HotelOwners修改DTO")
public class HotelOwnersUpdateDTO {

    @Schema(description =  "酒店入驻代表人ID")
    private Long ownerId;

    @Schema(description =  "关联的酒店ID")
    private Long hotelId;

    @Schema(description =  "代表人姓名")
    private String name;

    @Schema(description =  "代表人邮箱")
    private String email;

    @Schema(description =  "代表人联系电话")
    private String phone;
//
//    @Schema(description =  "加密后的密码")
//    private String passwordHash;
//
//    @Schema(description =  "注册时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createdAt;
//
//    @Schema(description =  "删除标识，0为未删除，1为已删除")
//    private String isDeleted;

}