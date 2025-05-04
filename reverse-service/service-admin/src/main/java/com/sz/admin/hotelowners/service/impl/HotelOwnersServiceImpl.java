package com.sz.admin.hotelowners.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.botsuch.rpcstarter.annotation.RpcService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.sz.admin.users.pojo.po.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sz.admin.hotelowners.service.HotelOwnersService;
import com.sz.admin.hotelowners.pojo.po.HotelOwners;
import com.sz.admin.hotelowners.mapper.HotelOwnersMapper;
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
import java.util.function.Consumer;

import com.sz.admin.hotelowners.pojo.dto.HotelOwnersCreateDTO;
import com.sz.admin.hotelowners.pojo.dto.HotelOwnersUpdateDTO;
import com.sz.admin.hotelowners.pojo.dto.HotelOwnersListDTO;
import com.sz.admin.hotelowners.pojo.dto.HotelOwnersImportDTO;
import com.sz.core.common.entity.ImportExcelDTO;
import com.sz.excel.core.ExcelResult;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import com.sz.excel.utils.ExcelUtils;
import lombok.SneakyThrows;
import com.sz.admin.hotelowners.pojo.vo.HotelOwnersVO;

/**
 * <p>
 * 酒店入驻代表人表 服务实现类
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Service
@RequiredArgsConstructor
@RpcService
public class HotelOwnersServiceImpl extends ServiceImpl<HotelOwnersMapper, HotelOwners> implements HotelOwnersService {

    private String getEncoderPwd(String pwd) {
        return BCrypt.hashpw(pwd, BCrypt.gensalt(10));
    }

    @Override
    public void create(HotelOwnersCreateDTO dto){
        // 用户的创建均判定手机号和邮箱唯一
        QueryWrapper phoneWrapper = QueryWrapper.create().eq(HotelOwners::getPhone, dto.getPhone());
        CommonResponseEnum.EMAIL_EXISTS.assertTrue(count(phoneWrapper) > 0);
        QueryWrapper emailWrapper = QueryWrapper.create().eq(HotelOwners::getEmail, dto.getEmail());
        CommonResponseEnum.PHONE_EXISTS.assertTrue(count(emailWrapper) > 0);

        HotelOwners hotelOwners = BeanCopyUtils.copy(dto, HotelOwners.class);
        String encodePwd = getEncoderPwd(dto.getPassword());
        hotelOwners.setPasswordHash(encodePwd);
        save(hotelOwners);
    }

    @Override
    public void update(HotelOwnersUpdateDTO dto){
        // todo ownerId从sa-token中获取填入

        HotelOwners hotelOwners = BeanCopyUtils.copy(dto, HotelOwners.class);
        // id有效性校验--如若删除之类的
        QueryWrapper ownerWrapper = QueryWrapper.create()
            .eq(HotelOwners::getOwnerId, dto.getOwnerId());
        CommonResponseEnum.INVALID.message("账号异常或已不存在").assertTrue(count(ownerWrapper) <= 0);

        // 构建唯一性检查 QueryWrapper
        QueryWrapper uniqueCheckWrapper = QueryWrapper.create()
                .ne(HotelOwners::getOwnerId, dto.getOwnerId())
                .and((Consumer<QueryWrapper>) wrapper -> wrapper // 使用 `and` 将 `OR` 条件组合在一起
                        .or((Consumer<QueryWrapper>) wrapper1 -> wrapper1.eq(HotelOwners::getPhone, dto.getPhone())) // 检查手机号
                        .or((Consumer<QueryWrapper>) wrapper1 -> wrapper1.eq(HotelOwners::getEmail, dto.getEmail())) // 检查邮箱
                        .or((Consumer<QueryWrapper>) wrapper1 -> wrapper1.eq(HotelOwners::getHotelId, dto.getHotelId())) // 检查酒店ID

                );

        // 执行唯一性检查
        Long uniqueCount = count(uniqueCheckWrapper);
        CommonResponseEnum.INVALID.message("信息已被占用，请再次检查绑定信息").assertTrue(uniqueCount!=0);
        saveOrUpdate(hotelOwners);
    }

    @Override
    public PageResult<HotelOwnersVO> page(HotelOwnersListDTO dto){
        Page<HotelOwnersVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), HotelOwnersVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<HotelOwnersVO> list(HotelOwnersListDTO dto){
        return listAs(buildQueryWrapper(dto), HotelOwnersVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto){
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    public HotelOwnersVO detail(Object id){
        HotelOwners hotelOwners = getById((Serializable) id);
        CommonResponseEnum.INVALID_ID.assertNull(hotelOwners);
        return BeanCopyUtils.copy(hotelOwners, HotelOwnersVO.class);
    }

    @SneakyThrows
    @Override
    public void importExcel(ImportExcelDTO dto) {
        ExcelResult<HotelOwnersImportDTO> excelResult = ExcelUtils.importExcel(dto.getFile().getInputStream(), HotelOwnersImportDTO.class, true);
        List<HotelOwnersImportDTO> list = excelResult.getList();
        List<String> errorList = excelResult.getErrorList();
        String analysis = excelResult.getAnalysis();
        System.out.println(" analysis : " + analysis);
        System.out.println(" isCover : " + dto.getIsCover());
    }

    @SneakyThrows
    @Override
    public void exportExcel(HotelOwnersListDTO dto, HttpServletResponse response) {
        List<HotelOwnersVO> list = list(dto);
        ServletOutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(list, "酒店入驻代表人表", HotelOwnersVO.class, os);
    }

    private static QueryWrapper buildQueryWrapper(HotelOwnersListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(HotelOwners.class);
        if (Utils.isNotNull(dto.getHotelId())) {
            wrapper.eq(HotelOwners::getHotelId, dto.getHotelId());
        }
        if (Utils.isNotNull(dto.getName())) {
            wrapper.like(HotelOwners::getName, dto.getName());
        }
        if (Utils.isNotNull(dto.getEmail())) {
            wrapper.eq(HotelOwners::getEmail, dto.getEmail());
        }
        if (Utils.isNotNull(dto.getPhone())) {
            wrapper.eq(HotelOwners::getPhone, dto.getPhone());
        }
//        if (Utils.isNotNull(dto.getPasswordHash())) {
//            wrapper.eq(HotelOwners::getPasswordHash, dto.getPasswordHash());
//        }
//        if (Utils.isNotNull(dto.getCreatedAtStart()) && Utils.isNotNull(dto.getCreatedAtEnd())) {
//            wrapper.between(HotelOwners::getCreatedAt, dto.getCreatedAtStart(), dto.getCreatedAtEnd());
//        }
//        if (Utils.isNotNull(dto.getIsDeleted())) {
//            wrapper.eq(HotelOwners::getIsDeleted, dto.getIsDeleted());
//        }
        return wrapper;
    }
}