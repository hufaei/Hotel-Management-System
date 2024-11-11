package com.sz.admin.system.pojo.po;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.sz.mysql.EntityChangeListener;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author sz
 * @since 2023-08-21
 */

@Data
@Table(value = "sys_role", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "系统角色表")
public class SysRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "角色id")
    private Long id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "简介")
    private String remark;

    @Schema(description = "删除与否")
    @Column(isLogicDelete = true)
    private String delFlag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Schema(description = "")
    private Long createId;

    @Schema(description = "")
    private Long updateId;

    @Schema(description = "是否锁定")
    private String isLock;

    @Schema(description = "标识，唯一")
    private String permissions;
}
