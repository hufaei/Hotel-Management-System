package com.sz.admin.rooms.service.impl;

import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.sz.admin.hotelowners.pojo.po.HotelOwners;
import com.sz.admin.hotels.pojo.po.Hotels;
import com.sz.admin.hotels.service.HotelsService;
import com.sz.platform.enums.BookingStatus;
import com.sz.platform.enums.RoomStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sz.admin.rooms.service.RoomsService;
import com.sz.admin.rooms.pojo.po.Rooms;
import com.sz.admin.rooms.mapper.RoomsMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
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
    private final HotelsService hotelsService;
    @Override
    public void create(RoomsCreateDTO dto){
        Rooms rooms = BeanCopyUtils.copy(dto, Rooms.class);

        Hotels hotel = hotelsService.getById(dto.getHotelId());
        CommonResponseEnum.INVALID_ID.assertNull(hotel);

        QueryWrapper wrapper = QueryWrapper.create().eq(Rooms::getHotelId,dto.getHotelId())
                .and((Consumer<QueryWrapper>) qw -> qw.eq(Rooms::getRoomNumber,dto.getRoomNumber()));
        // 检查是否存在多条记录
        Long count = count(wrapper);
        CommonResponseEnum.INVALID.message("已存在记录").assertTrue(count >= 1);
        rooms.setRoomStatus(RoomStatus.READY);
        save(rooms);
    }

    @Override
    public void toCheckIn(RoomsUpdateDTO dto){
        // 获取当前房间的信息--dto应该自动填充当前数据
        Rooms existingRoom = getById(dto.getRoomId());
        CommonResponseEnum.INVALID_ID.assertNull(existingRoom);

        CommonResponseEnum.VALID_ERROR.message("当前房间状态不可入住")
                .assertFalse(RoomStatus.READY.equals(existingRoom.getRoomStatus()));

        existingRoom.setRoomStatus(RoomStatus.CHECK_IN);

        saveOrUpdate(existingRoom);
    }
    @Override
    public void toReady(RoomsUpdateDTO dto){
        // 获取当前房间的信息--dto应该自动填充当前数据
        Rooms existingRoom = getById(dto.getRoomId());
        CommonResponseEnum.INVALID_ID.assertNull(existingRoom);
        // 仅更新房间状态
        if(existingRoom.getRoomStatus().equals(RoomStatus.READY)) return;

        existingRoom.setRoomStatus(RoomStatus.READY);

        saveOrUpdate(existingRoom);
    }
    @Override
    public void toCleaning(RoomsUpdateDTO dto){
        // 获取当前房间的信息--dto应该自动填充当前数据
        Rooms existingRoom = getById(dto.getRoomId());
        CommonResponseEnum.INVALID_ID.assertNull(existingRoom);
        // 仅更新房间状态
        if(existingRoom.getRoomStatus().equals(RoomStatus.CLEANING)) return;
        existingRoom.setRoomStatus(RoomStatus.CLEANING);

        saveOrUpdate(existingRoom);
    }

    @Override
    public PageResult<RoomsVO> page(RoomsListDTO dto){
        // todo 酒店owner可查-token取值
        Long ownerId = 1L;
        HotelOwners hotelOwners = QueryChain.of(HotelOwners.class).eq(HotelOwners::getOwnerId, ownerId).one();
        CommonResponseEnum.INVALID_TOKEN.message("该酒店账号不存在").assertNull(hotelOwners);
        // 只能查自身拥有的
        dto.setHotelId(hotelOwners.getHotelId());

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
            wrapper.eq(Rooms::getRoomType, dto.getRoomType());
        }
        if (Utils.isNotNull(dto.getRoomStatus())) {
            wrapper.eq(Rooms::getRoomStatus, dto.getRoomStatus());
        }
        return wrapper;
    }
}