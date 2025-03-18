package com.sz.admin.admins.service;

import com.mybatisflex.core.service.IService;
import com.sz.admin.admins.pojo.po.Admins;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.core.common.entity.PageResult;
import java.util.List;
import com.sz.admin.admins.pojo.dto.AdminsCreateDTO;
import com.sz.admin.admins.pojo.dto.AdminsUpdateDTO;
import com.sz.admin.admins.pojo.dto.AdminsListDTO;
import com.sz.admin.admins.pojo.vo.AdminsVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 管理员信息表 Service
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
public interface AdminsService extends IService<Admins> {

    void create(AdminsCreateDTO dto);

    void update(AdminsUpdateDTO dto);

    PageResult<AdminsVO> page(AdminsListDTO dto);

    List<AdminsVO> list(AdminsListDTO dto);

    void remove(SelectIdsDTO dto);

    AdminsVO detail(Object id);

    void importExcel(ImportExcelDTO dto);

    void exportExcel(AdminsListDTO dto, HttpServletResponse response);

}