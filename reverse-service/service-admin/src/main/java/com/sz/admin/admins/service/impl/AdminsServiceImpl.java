package com.sz.admin.admins.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sz.admin.admins.service.AdminsService;
import com.sz.admin.admins.pojo.po.Admins;
import com.sz.admin.admins.mapper.AdminsMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.query.QueryChain;
import com.sz.core.common.enums.CommonResponseEnum;
import com.sz.core.util.PageUtils;
import com.sz.core.util.BeanCopyUtils;
import com.sz.core.util.Utils;
import com.sz.core.common.entity.PageResult;
import com.sz.core.common.entity.SelectIdsDTO;
import java.io.Serializable;
import java.util.List;
import com.sz.admin.admins.pojo.dto.AdminsCreateDTO;
import com.sz.admin.admins.pojo.dto.AdminsUpdateDTO;
import com.sz.admin.admins.pojo.dto.AdminsListDTO;
import com.sz.admin.admins.pojo.dto.AdminsImportDTO;
import com.sz.core.common.entity.ImportExcelDTO;
import com.sz.excel.core.ExcelResult;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import com.sz.excel.utils.ExcelUtils;
import lombok.SneakyThrows;
import com.sz.admin.admins.pojo.vo.AdminsVO;

/**
 * <p>
 * 管理员信息表 服务实现类
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Service
@RequiredArgsConstructor
public class AdminsServiceImpl extends ServiceImpl<AdminsMapper, Admins> implements AdminsService {
    @Override
    public void create(AdminsCreateDTO dto){
        Admins admins = BeanCopyUtils.copy(dto, Admins.class);
        long count;
        save(admins);
    }

    @Override
    public void update(AdminsUpdateDTO dto){
        Admins admins = BeanCopyUtils.copy(dto, Admins.class);
        QueryWrapper wrapper;
        // id有效性校验
        wrapper = QueryWrapper.create()
            .eq(Admins::getAdminId, dto.getAdminId());
        CommonResponseEnum.INVALID_ID.assertTrue(count(wrapper) <= 0);

        // 唯一性校验
        long count;
        count = QueryChain.of(Admins.class).eq(Admins::getAdminId, dto.getAdminId()).ne(Admins::getAdminId, dto.getAdminId()).count();
        CommonResponseEnum.EXISTS.message("adminId已存在").assertTrue(count > 0);
        saveOrUpdate(admins);
    }

    @Override
    public PageResult<AdminsVO> page(AdminsListDTO dto){
        Page<AdminsVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), AdminsVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<AdminsVO> list(AdminsListDTO dto){
        return listAs(buildQueryWrapper(dto), AdminsVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto){
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    public AdminsVO detail(Object id){
        Admins admins = getById((Serializable) id);
        CommonResponseEnum.INVALID_ID.assertNull(admins);
        return BeanCopyUtils.copy(admins, AdminsVO.class);
    }

    @SneakyThrows
    @Override
    public void importExcel(ImportExcelDTO dto) {
        ExcelResult<AdminsImportDTO> excelResult = ExcelUtils.importExcel(dto.getFile().getInputStream(), AdminsImportDTO.class, true);
        List<AdminsImportDTO> list = excelResult.getList();
        List<String> errorList = excelResult.getErrorList();
        String analysis = excelResult.getAnalysis();
        System.out.println(" analysis : " + analysis);
        System.out.println(" isCover : " + dto.getIsCover());
    }

    @SneakyThrows
    @Override
    public void exportExcel(AdminsListDTO dto, HttpServletResponse response) {
        List<AdminsVO> list = list(dto);
        ServletOutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(list, "管理员信息表", AdminsVO.class, os);
    }

    private static QueryWrapper buildQueryWrapper(AdminsListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(Admins.class);
        if (Utils.isNotNull(dto.getAdminId())) {
            wrapper.eq(Admins::getAdminId, dto.getAdminId());
        }
        if (Utils.isNotNull(dto.getUsername())) {
            wrapper.eq(Admins::getUsername, dto.getUsername());
        }
        if (Utils.isNotNull(dto.getPasswordHash())) {
            wrapper.eq(Admins::getPasswordHash, dto.getPasswordHash());
        }
        if (Utils.isNotNull(dto.getEmail())) {
            wrapper.eq(Admins::getEmail, dto.getEmail());
        }
        if (Utils.isNotNull(dto.getCreatedAtStart()) && Utils.isNotNull(dto.getCreatedAtEnd())) {
            wrapper.between(Admins::getCreatedAt, dto.getCreatedAtStart(), dto.getCreatedAtEnd());
        }
        return wrapper;
    }
}