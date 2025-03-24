package com.sz.security.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.sz.core.common.annotation.DebounceIgnore;
import com.sz.core.common.entity.ApiResult;
import com.sz.security.pojo.LoginInfo;
import com.sz.security.pojo.LoginVO;
import com.sz.security.pojo.PasswordResetVo;
import com.sz.security.service.AuthService;
import com.sz.security.service.ResetAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用controller--登录登出接口
 *
 * @ClassName CommonController
 * @Author botsuchi
 * @Date 2024/10/31
 * @Version 1.1
 */
@Tag(name = "认证")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final ResetAuthService resetAuthService;

    @DebounceIgnore
    @Operation(summary = "登录")
    @PostMapping("login")
    public ApiResult<LoginVO> login(@RequestBody LoginInfo loginInfo) {
        return ApiResult.success(authService.loginClient(loginInfo));
    }

    @Operation(summary = "登出")
    @PostMapping("logout")
    public ApiResult logout() {
        // 注意执行顺序，最后再执行logout
        StpUtil.getTokenSession().logout(); // 清除缓存session
        StpUtil.logout();
        return ApiResult.success();
    }

    @Operation(summary = "修改密码")
    @PostMapping("resetPassword")
    public ApiResult resetPassword(@RequestBody PasswordResetVo resetVo) {
        resetAuthService.resetPassword(resetVo);
        // 注意执行顺序，最后再执行logout
        StpUtil.getTokenSession().logout(); // 清除缓存session
        StpUtil.logout();
        return ApiResult.success();
    }

}
