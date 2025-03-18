package com.sz.admin.users.pojo.po;

import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.io.Serial;
import com.sz.mysql.EntityChangeListener;
import java.time.LocalDateTime;

/**
* <p>
* 用户信息表
* </p>
*
* @author botsuchi
* @since 2024-10-27
*/
@Data
@Table(value = "users", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "用户信息表")
public class Users implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description ="用户ID")
    private Long userId;

    @Schema(description ="用户名")
    private String username;

    @Schema(description ="用户邮箱，用于登录和接收通知")
    private String email;

    @Schema(description ="用户手机号码")
    private String phone;

    @Schema(description ="加密后的密码")
    private String passwordHash;

    @Schema(description ="注册时间")
    private LocalDateTime createdAt;

    @Column(isLogicDelete = true)
    @Schema(description ="删除标识，0为未删除，1为已删除")
    private String isDeleted;

}