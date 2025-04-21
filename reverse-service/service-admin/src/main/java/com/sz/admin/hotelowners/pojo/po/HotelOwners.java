package com.sz.admin.hotelowners.pojo.po;

import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.io.Serial;
import com.sz.mysql.EntityChangeListener;
import java.time.LocalDateTime;

/**
* <p>
* 酒店入驻代表人表
* </p>
*
* @author botsuchi
* @since 2024-10-27
*/
@Data
@Table(value = "hotel_owners", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "酒店后台人员表")
public class HotelOwners implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description ="酒店后台ID")
    private String ownerId;

    @Schema(description ="关联的酒店ID")
    private String hotelId;

    @Schema(description ="代表人姓名")
    private String name;

    @Schema(description ="代表人邮箱")
    private String email;

    @Schema(description ="代表人联系电话")
    private String phone;

    @Schema(description ="加密后的密码")
    private String passwordHash;

    @Schema(description ="注册时间")
    private LocalDateTime createdAt;

    @Column(isLogicDelete = true)
    @Schema(description ="删除标识，0为未删除，1为已删除")
    private String isDeleted;

}