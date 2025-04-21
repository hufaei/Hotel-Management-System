package com.sz.core.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName UserInfo
 * @Author sz
 * @Date 2023/12/12 9:53
 * @Version 1.0
 */
@Data
public class BaseUserInfo {

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "用户名")
    private String username;
//
//    @Schema(description = "昵称")
//    private String nickname;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "如果是前台就有这个关联酒店的Id")
    private String ownerHotelId;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "如果是用户显示余额")
    private BigDecimal balance;

}
