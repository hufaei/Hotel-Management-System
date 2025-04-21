package com.sz.admin.users.service;

import com.mybatisflex.core.service.IService;
import com.sz.admin.users.pojo.po.Users;
import com.sz.core.common.entity.LoginUser;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.core.common.entity.PageResult;

import java.math.BigDecimal;
import java.util.List;
import com.sz.admin.users.pojo.dto.UsersCreateDTO;
import com.sz.admin.users.pojo.dto.UsersUpdateDTO;
import com.sz.admin.users.pojo.dto.UsersListDTO;
import com.sz.admin.users.pojo.vo.UsersVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户信息表 Service
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
public interface UsersService extends IService<Users> {

    void create(UsersCreateDTO dto);;

    void update(UsersUpdateDTO dto);

    PageResult<UsersVO> page(UsersListDTO dto);

    List<UsersVO> list(UsersListDTO dto);

    void remove(SelectIdsDTO dto);

    void freeze(BigDecimal count);

    UsersVO detailByPhoneOremail(String phone);


    LoginUser buildLoginUser(String username, String password);

    UsersVO detail(Object id);

    void unfreeze(BigDecimal amount ,Boolean bool);
}