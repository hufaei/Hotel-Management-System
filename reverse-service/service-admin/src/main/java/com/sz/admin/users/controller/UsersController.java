package com.sz.admin.users.controller;

import com.mybatisflex.core.query.QueryWrapper;
import com.sz.admin.users.pojo.po.Users;
import com.sz.core.common.enums.CommonResponseEnum;
import com.sz.core.util.EmailUtils;
import com.sz.core.util.ValidateCodeUtils;
import com.sz.security.service.EmailCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import com.sz.core.common.entity.ApiPageResult;
import com.sz.core.common.entity.ApiResult;
import com.sz.core.common.constant.GlobalConstant;
import com.sz.core.common.entity.PageResult;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.admin.users.service.UsersService;
import com.sz.admin.users.pojo.dto.UsersCreateDTO;
import com.sz.admin.users.pojo.dto.UsersUpdateDTO;
import com.sz.admin.users.pojo.dto.UsersListDTO;
import com.sz.admin.users.pojo.vo.UsersVO;

import java.util.Map;

/**
 * <p>
 * 用户信息表 Controller
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Tag(name =  "用户信息表")
@RestController
@RequestMapping("users")
@RequiredArgsConstructor // 只于final字段构造函数
public class UsersController  {

    private final UsersService usersService;
    private final EmailCodeService emailCodeService;

    @Operation(summary = "新增（注册）")
    @PostMapping
    public ApiResult create(@RequestBody UsersCreateDTO dto) {
        usersService.create(dto);
        return ApiResult.success();
    }

    @Operation(summary = "修改")
    @PutMapping
    public ApiResult update(@RequestBody UsersUpdateDTO dto) {
        // 传入属性值为null则不更新此字段
        usersService.update(dto);
        return ApiResult.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        usersService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
//    @SaCheckPermission(value = "users.query_table")
    @GetMapping
    public ApiResult<PageResult<UsersVO>> list(UsersListDTO dto) {
        return ApiPageResult.success(usersService.page(dto));
    }

    @Operation(summary = "检索详情")
    @GetMapping("/{keywords}")
    public ApiResult<UsersVO> detailByKeywords(@PathVariable String keywords) {
        return ApiResult.success(usersService.detailByPhoneOremail(keywords));
    }

    @Operation(summary = "详情")
    @GetMapping("/detail/{id}")
    public ApiResult<UsersVO> detail(@PathVariable Object id) {
        return ApiResult.success(usersService.detail(id));
    }

    @Operation(summary = "发送验证码")
    @GetMapping("/sendEmail/{email}")
    public ApiResult<Boolean> sendEmail(@PathVariable("email") String email) throws MessagingException {
        Integer authCode = ValidateCodeUtils.generateValidateCode();
        EmailUtils.sendHtmlEmail(email, String.valueOf(authCode));

        // 存储验证码到Redis，5分钟过期
        emailCodeService.saveAuthCode(email, String.valueOf(authCode));
        return ApiResult.success(true);
    }

    @Operation(summary = "验证码校验")
    @PostMapping("/verifyCode")
    public ApiResult<Boolean> verifyCode(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("Remail");
        String code = requestBody.get("code");
        System.out.println(requestBody);

        // 从Redis中获取验证码
        String redisCode = emailCodeService.getAuthCode(email);

        emailCodeService.deleteAuthCode(email);
        CommonResponseEnum.INVALID_TOKEN.message("验证码错误").assertFalse(code.equals(redisCode));
        // 验证后删除验证码，防止重复使用
        emailCodeService.deleteAuthCode(email);


        return ApiResult.success(true);
    }
    @Operation(summary = "邮箱校验")
    @GetMapping("/verifyEmail/{email}")
    public ApiResult<Boolean> verifyEmail(@PathVariable("email") String email) {
        // 检查邮箱是否存在
        QueryWrapper wrapper = QueryWrapper.create().from(Users.class);
        wrapper.eq(Users::getEmail, email);
        Users user = usersService.getOne(wrapper);
        CommonResponseEnum.INVALID_TOKEN.assertNull(user, "邮箱不存在");

        return ApiResult.success(true);
    }
}