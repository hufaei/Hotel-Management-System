package com.sz.security.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName PasswordResetVo
 * @Author li
 * @Date 2024/1/22 15:35
 * @Version 1.0
 */
@Data
@Schema(description = "修改密码验证视图")
public class PasswordResetVo {

    @Schema(description = "username")
    private String username;

    @Schema(description = "newPwd 新密码")
    private String newPwd;

}
