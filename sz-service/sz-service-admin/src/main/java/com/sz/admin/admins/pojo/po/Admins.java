package com.sz.admin.admins.pojo.po;

import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.io.Serial;
import com.sz.mysql.EntityChangeListener;
import java.time.LocalDateTime;

/**
* <p>
* 管理员信息表
* </p>
*
* @author botsuchi
* @since 2024-10-27
*/
@Data
@Table(value = "admins", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "管理员信息表")
public class Admins implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description ="管理员ID")
    private Long adminId;

    @Schema(description ="管理员用户名")
    private String username;

    @Schema(description ="加密后的管理员密码")
    private String passwordHash;

    @Schema(description ="管理员邮箱，用于接收系统通知")
    private String email;

    @Schema(description ="创建时间")
    private LocalDateTime createdAt;

}