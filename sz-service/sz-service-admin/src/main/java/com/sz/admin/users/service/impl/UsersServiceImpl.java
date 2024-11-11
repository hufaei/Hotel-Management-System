package com.sz.admin.users.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.sz.admin.system.pojo.po.SysUser;
import com.sz.admin.system.pojo.vo.sysuser.SysUserVO;
import com.sz.core.common.entity.*;
import com.sz.core.util.*;
import com.sz.redis.CommonKeyConstants;
import com.sz.redis.RedisCache;
import com.sz.redis.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import com.sz.admin.users.service.UsersService;
import com.sz.admin.users.pojo.po.Users;
import com.sz.admin.users.mapper.UsersMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.sz.core.common.enums.CommonResponseEnum;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.sz.admin.users.pojo.dto.UsersCreateDTO;
import com.sz.admin.users.pojo.dto.UsersUpdateDTO;
import com.sz.admin.users.pojo.dto.UsersListDTO;
import com.sz.admin.users.pojo.dto.UsersImportDTO;
import com.sz.excel.core.ExcelResult;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import com.sz.excel.utils.ExcelUtils;
import lombok.SneakyThrows;
import com.sz.admin.users.pojo.vo.UsersVO;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Service
@RequiredArgsConstructor
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    private final RedisCache redisCache;

    // 注册用户所使用的加解密方法
    private String getEncoderPwd(String pwd) {
        return BCrypt.hashpw(pwd, BCrypt.gensalt(10));
    }

    private boolean matchEncoderPwd(String pwd, String pwdEncoder) {
        return BCrypt.checkpw(pwd, pwdEncoder);
    }

    @Override
    public void create(UsersCreateDTO dto){
        Users users = BeanCopyUtils.copy(dto, Users.class);
        QueryWrapper phoneWrapper = QueryWrapper.create().eq(Users::getPhone, dto.getPhone());
        CommonResponseEnum.PHONE_EXISTS.assertTrue(count(phoneWrapper) > 0);
        QueryWrapper emailWrapper = QueryWrapper.create().eq(Users::getEmail, dto.getEmail());
        CommonResponseEnum.EMAIL_EXISTS.assertTrue(count(emailWrapper) > 0);
        // 填入数据
        String encodePwd = getEncoderPwd(dto.getPassword());
        users.setPasswordHash(encodePwd);
        save(users);
    }


    @Override
    public void update(UsersUpdateDTO dto){
        // todo userId从sa-token中获取主键填入

        Users users = BeanCopyUtils.copy(dto, Users.class);
        QueryWrapper wrapper;

        // id有效性校验
        wrapper = QueryWrapper.create()
            .eq(Users::getUserId, dto.getUserId());// Lambda表达式避免硬编码
        CommonResponseEnum.INVALID.message("账号异常或已不存在").assertTrue(count(wrapper) <= 0);
        // 基于主键更新
        saveOrUpdate(users);
    }

    @Override
    public PageResult<UsersVO> page(UsersListDTO dto){
        Page<UsersVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), UsersVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<UsersVO> list(UsersListDTO dto){
        return listAs(buildQueryWrapper(dto), UsersVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto){
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    // 原则上避免id直接传输
    public UsersVO detailByPhoneOremail(String phoneOrEmail) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(Users::getPhone,phoneOrEmail)
                .or((Consumer<QueryWrapper>) qw -> qw.eq(Users::getEmail,phoneOrEmail));

        // 检查是否存在多条记录
        Long count = count(queryWrapper);
        CommonResponseEnum.INVALID.assertTrue(count >= 1);
        // 查询单条记录
        Users user = getOne(queryWrapper);
        return BeanCopyUtils.copy(user, UsersVO.class);
    }


    @SneakyThrows
    @Override
    public void importExcel(ImportExcelDTO dto) {
        ExcelResult<UsersImportDTO> excelResult = ExcelUtils.importExcel(dto.getFile().getInputStream(), UsersImportDTO.class, true);
        List<UsersImportDTO> list = excelResult.getList();
        List<String> errorList = excelResult.getErrorList();
        String analysis = excelResult.getAnalysis();
        System.out.println(" analysis : " + analysis);
        System.out.println(" isCover : " + dto.getIsCover());
    }

    @SneakyThrows
    @Override
    public void exportExcel(UsersListDTO dto, HttpServletResponse response) {
        List<UsersVO> list = list(dto);
        ServletOutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(list, "用户信息表", UsersVO.class, os);
    }

    private static QueryWrapper buildQueryWrapper(UsersListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(Users.class);
        if (Utils.isNotNull(dto.getUserId())) {
            wrapper.eq(Users::getUserId, dto.getUserId());
        }
        if (Utils.isNotNull(dto.getUsername())) {
            wrapper.like(Users::getUsername, dto.getUsername());
        }
        if (Utils.isNotNull(dto.getEmail())) {
            wrapper.eq(Users::getEmail, dto.getEmail());
        }
        if (Utils.isNotNull(dto.getPhone())) {
            wrapper.eq(Users::getPhone, dto.getPhone());
        }
//        if (Utils.isNotNull(dto.getPasswordHash())) {
//            wrapper.eq(Users::getPasswordHash, dto.getPasswordHash());
//        }
        if (Utils.isNotNull(dto.getCreatedAtStart()) && Utils.isNotNull(dto.getCreatedAtEnd())) {
            wrapper.between(Users::getCreatedAt, dto.getCreatedAtStart(), dto.getCreatedAtEnd());
        }
        if (Utils.isNotNull(dto.getIsDeleted())) {
            wrapper.eq(Users::getIsDeleted, dto.getIsDeleted());
        }
        return wrapper;
    }
    @Override
    public LoginUser buildLoginUser(String username, String password) {

        // todo 布隆校验——防止爆破登录
        boolean hasKey = RedisUtils.hasKey(CommonKeyConstants.SYS_PWD_ERR_CNT, username);
        Object value = RedisUtils.getValue(CommonKeyConstants.SYS_PWD_ERR_CNT, username);
        long count = hasKey ? Long.parseLong(String.valueOf(value)) : 0;

        String maxErrCnt = SysConfigUtils.getConfValue("sys.pwd.errCnt");
        CommonResponseEnum.CNT_PASSWORD_ERR.assertTrue(hasKey && (count >= Utils.getIntVal(maxErrCnt)));

        // 密码校验
        UsersVO userVo = detailByPhoneOremail(username);
        Long userId = userVo.getUserId();
        String timeout = SysConfigUtils.getConfValue("sys_pwd.lockTime");
        boolean checkpwd = BCrypt.checkpw(password, userVo.getPasswordHash());
        if (!checkpwd)
            redisCache.countPwdErr(username, Utils.getLongVal(timeout).longValue());
        CommonResponseEnum.BAD_USERNAME_OR_PASSWORD.assertFalse(checkpwd);

        LoginUser loginUser = getLoginUser(userId, userVo);
        return loginUser;
    }
    @NotNull
    private LoginUser getLoginUser(Long userId, UsersVO userVo) {
        BaseUserInfo userInfo = BeanCopyUtils.springCopy(userVo, BaseUserInfo.class);
        Users user = QueryChain.of(Users.class).eq(Users::getUserId, userId).one();
        CommonResponseEnum.INVALID_USER.assertNull(user);
        LoginUser loginUser = new LoginUser();
        loginUser.setUserInfo(userInfo);

        return loginUser;
    }

}