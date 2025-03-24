package com.sz.security.service;

import com.sz.security.pojo.PasswordResetVo;

/**
 * @ClassName AuthService
 * @Author li
 * @Date 2024/1/22 17:24
 * @Version 1.0
 */
import com.sz.security.pojo.PasswordResetVo;

public interface ResetAuthService {
    void resetPassword(PasswordResetVo resetVo);
}
