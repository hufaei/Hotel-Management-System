package com.sz.admin.rooms.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.sz.platform.enums.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sz.admin.rooms.service.RoomsService;
import com.sz.admin.rooms.pojo.po.Rooms;
import com.sz.admin.rooms.mapper.RoomsMapper;
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

import com.sz.admin.rooms.pojo.dto.RoomsCreateDTO;
import com.sz.admin.rooms.pojo.dto.RoomsUpdateDTO;
import com.sz.admin.rooms.pojo.dto.RoomsListDTO;
import com.sz.admin.rooms.pojo.dto.RoomsImportDTO;
import com.sz.core.common.entity.ImportExcelDTO;
import com.sz.excel.core.ExcelResult;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import com.sz.excel.utils.ExcelUtils;
import lombok.SneakyThrows;
import com.sz.admin.rooms.pojo.vo.RoomsVO;

/**
 * <p>
 * 房间信息表 服务实现类
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Service
@RequiredArgsConstructor
public class RoomsServiceImpl extends ServiceImpl<RoomsMapper, Rooms> implements RoomsService {
    @Override
    public void create(RoomsCreateDTO dto){
        Rooms rooms = BeanCopyUtils.copy(dto, Rooms.class);
        QueryWrapper wrapper = QueryWrapper.create().eq(Rooms::getHotelId,dto.getHotelId())
                .and((Consumer<QueryWrapper>) qw -> qw.eq(Rooms::getRoomNumber,dto.getRoomNumber()));
        // 检查是否存在多条记录
        Long count = count(wrapper);
        CommonResponseEnum.INVALID.assertTrue(count >= 1);
        save(rooms);
    }

    @Override
    public void update(RoomsUpdateDTO dto){
        // 获取当前房间的信息--dto应该自动填充当前数据
        Rooms existingRoom = getById(dto.getRoomId());
        CommonResponseEnum.INVALID_ID.assertNull(existingRoom);

        Rooms rooms = BeanCopyUtils.copy(dto, Rooms.class);

        // 检查是否存在同宾馆的房间号重复
        QueryWrapper checkWrapper = QueryWrapper.create()
                .eq(Rooms::getHotelId, rooms.getHotelId())
                .and((Consumer<QueryWrapper>) qw -> qw.eq(Rooms::getRoomNumber, rooms.getRoomNumber())
                        .ne(Rooms::getRoomId, rooms.getRoomId())); // 排除当前房间号

        Long count = count(checkWrapper);
        CommonResponseEnum.INVALID.message("数据重复").assertTrue(count != 0);

        saveOrUpdate(rooms);
    }

    @Override
    public PageResult<RoomsVO> page(RoomsListDTO dto){
        Page<RoomsVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), RoomsVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<RoomsVO> list(RoomsListDTO dto){
        return listAs(buildQueryWrapper(dto), RoomsVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto){
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    public RoomsVO detail(Object id){
        Rooms rooms = getById((Serializable) id);
        CommonResponseEnum.INVALID_ID.assertNull(rooms);
        return BeanCopyUtils.copy(rooms, RoomsVO.class);
    }

    @SneakyThrows
    @Override
    public void importExcel(ImportExcelDTO dto) {
        ExcelResult<RoomsImportDTO> excelResult = ExcelUtils.importExcel(dto.getFile().getInputStream(), RoomsImportDTO.class, true);
        List<RoomsImportDTO> list = excelResult.getList();
        List<String> errorList = excelResult.getErrorList();
        String analysis = excelResult.getAnalysis();
        System.out.println(" analysis : " + analysis);
        System.out.println(" isCover : " + dto.getIsCover());
    }

    @SneakyThrows
    @Override
    public void exportExcel(RoomsListDTO dto, HttpServletResponse response) {
        List<RoomsVO> list = list(dto);
        ServletOutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(list, "房间信息表", RoomsVO.class, os);
    }

    private static QueryWrapper buildQueryWrapper(RoomsListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(Rooms.class);
        if (Utils.isNotNull(dto.getHotelId())) {
            wrapper.eq(Rooms::getHotelId, dto.getHotelId());
        }
        if (Utils.isNotNull(dto.getRoomNumber())) {
            wrapper.eq(Rooms::getRoomNumber, dto.getRoomNumber());
        }
        if (Utils.isNotNull(dto.getRoomType())) {
            wrapper.eq(Rooms::getRoomType, RoomType.fromCode(dto.getRoomType()));
        }
        if (Utils.isNotNull(dto.getPrice())) {
            wrapper.lt(Rooms::getPrice, dto.getPrice());
        }
        if (Utils.isNotNull(dto.getSize())) {
            wrapper.eq(Rooms::getSize, dto.getSize());
        }
        if (Utils.isNotNull(dto.getBedInfo())) {
            wrapper.eq(Rooms::getBedInfo, dto.getBedInfo());
        }
        if (Utils.isNotNull(dto.getCapacity())) {
            wrapper.eq(Rooms::getCapacity, dto.getCapacity());
        }
        if (Utils.isNotNull(dto.getAvailability())) {
            wrapper.eq(Rooms::getAvailability, dto.getAvailability());
        }
        return wrapper;
    }
}