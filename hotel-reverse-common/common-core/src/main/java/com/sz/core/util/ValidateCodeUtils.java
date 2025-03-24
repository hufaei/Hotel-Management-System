package com.sz.core.util;

import java.util.Random;

public class ValidateCodeUtils {
    /**
     * 随机生成六位数验证码
     *
     * @return Integer
     */
    public static Integer generateValidateCode() {
        Integer code = new Random().nextInt(999999);
        if (code < 100000) {
            //保证随机数为6位数字
            code = code + 100000;
        }
        return code;
    }
}
