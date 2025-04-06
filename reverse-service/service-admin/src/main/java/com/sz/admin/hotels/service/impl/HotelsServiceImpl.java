package com.sz.admin.hotels.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.sz.admin.hotelowners.pojo.po.HotelOwners;
import com.sz.admin.hotelowners.service.HotelOwnersService;
import com.sz.admin.reviews.service.ReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sz.admin.hotels.service.HotelsService;
import com.sz.admin.hotels.pojo.po.Hotels;
import com.sz.admin.hotels.mapper.HotelsMapper;
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
import java.util.Objects;

import com.sz.admin.hotels.pojo.dto.HotelsCreateDTO;
import com.sz.admin.hotels.pojo.dto.HotelsUpdateDTO;
import com.sz.admin.hotels.pojo.dto.HotelsListDTO;
import com.sz.core.common.entity.ImportExcelDTO;
import com.sz.excel.core.ExcelResult;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import com.sz.excel.utils.ExcelUtils;
import lombok.SneakyThrows;
import com.sz.admin.hotels.pojo.vo.HotelsVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 酒店信息表 服务实现类
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Service
@RequiredArgsConstructor
public class HotelsServiceImpl extends ServiceImpl<HotelsMapper, Hotels> implements HotelsService {

    private final HotelOwnersService hotelOwnersService;
    // 循环引用
//    private final ReviewsService reviewsService;


    @Transactional
    @Override
    public void create(HotelsCreateDTO dto){
        // todo 从sa-token中读取ownerId 事务更新双表
        Long ownerId = 2L;
        Integer stars = dto.getStars();
        Integer roomCount = dto.getRoomCount();

        QueryWrapper wrapper = QueryWrapper.create().eq(Hotels::getAddress, dto.getAddress());
        CommonResponseEnum.ADDRESS_EXISTS.assertTrue(count(wrapper) > 0);
        CommonResponseEnum.INVALID.message("星级信息错误").assertTrue(stars>5 || stars<0);
        CommonResponseEnum.INVALID.message("房间数量信息错误").assertTrue(roomCount < 0);
        Hotels hotels = BeanCopyUtils.copy(dto, Hotels.class);
        save(hotels);
//        System.out.println(hotels);

        // 更新 hotelOwner 表的 hotelId 字段__因为hotelId是主键，保存时应该不担心重复问题
        HotelOwners hotelOwner = hotelOwnersService.getById(ownerId);
        CommonResponseEnum.INVALID_ID.assertNull(hotelOwner);

        hotelOwner.setHotelId(hotels.getHotelId());  // 更新生成的 hotelId
        hotelOwnersService.updateById(hotelOwner);  // 保存更新后的 hotelOwner
    }

    @Override
    public void update(HotelsUpdateDTO dto){
        // todo 从sa-token中读取ownerId
        Long ownerId = 2L;
        // 更新时必须填充原所有数据
        Hotels hotels = BeanCopyUtils.copy(dto, Hotels.class);
        QueryWrapper wrapper;
        // id有效性校验
        wrapper = QueryWrapper.create()
            .eq(Hotels::getHotelId, dto.getHotelId());
        CommonResponseEnum.INVALID_ID.assertTrue(count(wrapper) <= 0);

        // 所有者校验
        HotelOwners hotelOwner = hotelOwnersService.getById(ownerId);
        CommonResponseEnum.INVALID_TOKEN.assertTrue(!Objects.equals(hotelOwner.getHotelId(), dto.getHotelId()));

        // 地址唯一性校验，排除当前更新的记录
        QueryWrapper addressWrapper = QueryWrapper.create()
                .eq(Hotels::getAddress, dto.getAddress())
                .ne(Hotels::getHotelId, dto.getHotelId());  // 排除当前更新的酒店ID
       CommonResponseEnum.ADDRESS_EXISTS.assertTrue(count(addressWrapper)!=0);


        saveOrUpdate(hotels);
    }

    @Override
    public PageResult<HotelsVO> page(HotelsListDTO dto){
        Page<HotelsVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), HotelsVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<HotelsVO> list(HotelsListDTO dto){
        return listAs(buildQueryWrapper(dto), HotelsVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto){
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    public HotelsVO detail(Object id){
        Hotels hotels = getById((Serializable) id);
        CommonResponseEnum.INVALID_ID.assertNull(hotels);
        return BeanCopyUtils.copy(hotels, HotelsVO.class);
    }

    @SneakyThrows
    @Override
    public void importExcel(ImportExcelDTO dto) {
//        ExcelResult<HotelsImportDTO> excelResult = ExcelUtils.importExcel(dto.getFile().getInputStream(), HotelsImportDTO.class, true);
//        List<HotelsImportDTO> list = excelResult.getList();
//        List<String> errorList = excelResult.getErrorList();
//        String analysis = excelResult.getAnalysis();
//        System.out.println(" analysis : " + analysis);
//        System.out.println(" isCover : " + dto.getIsCover());
    }

    @SneakyThrows // 自动“忽略”受检异常
    @Override
    public void exportExcel(HotelsListDTO dto, HttpServletResponse response) {
        List<HotelsVO> list = list(dto);
        ServletOutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(list, "酒店信息表", HotelsVO.class, os);
    }

    @Override
    public double getAvgScore(String hotelId) {
        // todo 计算平均分
        return 0;
    }

    private static QueryWrapper buildQueryWrapper(HotelsListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(Hotels.class);
        if (Utils.isNotNull(dto.getHotelId())) {
            wrapper.eq(Hotels::getHotelId, dto.getHotelId());
        }
        if (Utils.isNotNull(dto.getHotelName())) {
            wrapper.like(Hotels::getHotelName, dto.getHotelName());
        }
        if (Utils.isNotNull(dto.getProvince())) {
            wrapper.like(Hotels::getProvince, dto.getProvince());
        }
        if (Utils.isNotNull(dto.getCity())) {
            wrapper.like(Hotels::getCity, dto.getCity());
        }
        if (Utils.isNotNull(dto.getCountry())) {
            wrapper.like(Hotels::getCountry, dto.getCountry());
        }
        if (Utils.isNotNull(dto.getStars())) {
            wrapper.like(Hotels::getStars, dto.getStars());
        }
        if (Utils.isNotNull(dto.getAddress())) {
            wrapper.eq(Hotels::getAddress, dto.getAddress());
        }
        if (Utils.isNotNull(dto.getOpeningDateEnd()) && Utils.isNotNull(dto.getOpeningDateStart())) {
            wrapper.between(Hotels::getOpeningYear, dto.getOpeningDateStart(),dto.getOpeningDateEnd());
        }

        return wrapper;
    }
}