package com.sz.admin.bookings.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.sz.admin.rooms.pojo.po.Rooms;
import com.sz.admin.rooms.service.RoomsService;
import com.sz.platform.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sz.admin.bookings.service.BookingsService;
import com.sz.admin.bookings.pojo.po.Bookings;
import com.sz.admin.bookings.mapper.BookingsMapper;
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
import java.time.LocalDate;
import java.util.List;
import com.sz.admin.bookings.pojo.dto.BookingsCreateDTO;
import com.sz.admin.bookings.pojo.dto.BookingsUpdateDTO;
import com.sz.admin.bookings.pojo.dto.BookingsListDTO;
import com.sz.admin.bookings.pojo.dto.BookingsImportDTO;
import com.sz.core.common.entity.ImportExcelDTO;
import com.sz.excel.core.ExcelResult;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import com.sz.excel.utils.ExcelUtils;
import lombok.SneakyThrows;
import com.sz.admin.bookings.pojo.vo.BookingsVO;

/**
 * <p>
 * 预订信息表 服务实现类
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Service
@RequiredArgsConstructor
public class BookingsServiceImpl extends ServiceImpl<BookingsMapper, Bookings> implements BookingsService {
    private final RoomsService roomsService;
    @Override
    public void create(BookingsCreateDTO dto){
        Bookings bookings = BeanCopyUtils.copy(dto, Bookings.class);
        // 校验酒店和房间的有效性
        Long countRoom = QueryChain.of(Rooms.class)
                .eq(Rooms::getHotelId, dto.getHotelId())
                .eq(Rooms::getRoomId, dto.getRoomId()).count();

        CommonResponseEnum.INVALID.message("酒店信息无效").assertTrue(countRoom <= 0);

        // 日期二次校验
        LocalDate startDate = dto.getBookingDate();
        LocalDate endDate = dto.getBookingEnd();
        CommonResponseEnum.INVALID.message("开始日期不能早于今日").assertTrue(startDate.isBefore(LocalDate.now()));
        CommonResponseEnum.INVALID.message("结束日期不能早于开始日期").assertTrue(!startDate.isBefore(endDate));

        //todo 用户id从sa-token中获取
        bookings.setUserId(1);
        // 唯一性校验(房间号+时间)
        long count = QueryChain.of(Bookings.class).eq(Bookings::getRoomId, dto.getRoomId())
                                                .ge(Bookings::getBookingDate, startDate)  // 结束日期大于等于预订开始日期
                                                .le(Bookings::getBookingEnd, endDate)
                                                .count();
        CommonResponseEnum.EXISTS.message("该房间在预订日期已存在订单").assertTrue(count > 0);

        // 状态重置
        bookings.setStatus(BookingStatus.PENDING_CONFIRMATION);  // 设置默认状态
        save(bookings);
    }

    @Override
    public void confirm(BookingsUpdateDTO dto){
        Bookings bookings = BeanCopyUtils.copy(dto, Bookings.class);
        QueryWrapper wrapper;
        // 有效性校验——如果是待确认则能确认
        wrapper = QueryWrapper.create()
            .eq(Bookings::getBookingId, dto.getBookingId())
            .eq(Bookings::getStatus, BookingStatus.PENDING_CONFIRMATION);;
        CommonResponseEnum.UNKNOWN.message("不被允许的操作").assertTrue(count(wrapper) <= 0);


        bookings.setStatus(BookingStatus.CONFIRMED);
        saveOrUpdate(bookings);
    }
    @Override
    public void cancel(BookingsUpdateDTO dto){
        Bookings bookings = BeanCopyUtils.copy(dto, Bookings.class);
        QueryWrapper wrapper;
        // 有效性校验——待确认的才能能取消
        wrapper = QueryWrapper.create()
                .eq(Bookings::getBookingId, dto.getBookingId())
                .eq(Bookings::getStatus, BookingStatus.PENDING_CONFIRMATION);
        CommonResponseEnum.UNKNOWN.message("不被允许的操作").assertTrue(count(wrapper) <= 0);
        bookings.setStatus(BookingStatus.CANCELED);
        saveOrUpdate(bookings);
    }

    @Override
    public PageResult<BookingsVO> page(BookingsListDTO dto){
        // 查询用户订单直接传入sa-token对应id
        Page<BookingsVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), BookingsVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<BookingsVO> list(BookingsListDTO dto){
        return listAs(buildQueryWrapper(dto), BookingsVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto){
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    public BookingsVO detail(Object id){
        Bookings bookings = getById((Serializable) id);
        CommonResponseEnum.INVALID_ID.assertNull(bookings);
        return BeanCopyUtils.copy(bookings, BookingsVO.class);
    }

    @SneakyThrows
    @Override
    public void importExcel(ImportExcelDTO dto) {
        ExcelResult<BookingsImportDTO> excelResult = ExcelUtils.importExcel(dto.getFile().getInputStream(), BookingsImportDTO.class, true);
        List<BookingsImportDTO> list = excelResult.getList();
        List<String> errorList = excelResult.getErrorList();
        String analysis = excelResult.getAnalysis();
        System.out.println(" analysis : " + analysis);
        System.out.println(" isCover : " + dto.getIsCover());
    }

    @SneakyThrows
    @Override
    public void exportExcel(BookingsListDTO dto, HttpServletResponse response) {
        List<BookingsVO> list = list(dto);
        ServletOutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(list, "预订信息表", BookingsVO.class, os);
    }

    private static QueryWrapper buildQueryWrapper(BookingsListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(Bookings.class);
        if (Utils.isNotNull(dto.getUserId())) {
            wrapper.eq(Bookings::getUserId, dto.getUserId());
        }
        if (Utils.isNotNull(dto.getHotelId())) {
            wrapper.eq(Bookings::getHotelId, dto.getHotelId());
        }
        if (Utils.isNotNull(dto.getRoomId())) {
            wrapper.eq(Bookings::getRoomId, dto.getRoomId());
        }
        if (Utils.isNotNull(dto.getBookingDateStart()) && Utils.isNotNull(dto.getBookingDateEnd())) {
            wrapper.between(Bookings::getCreatedAt, dto.getBookingDateStart(), dto.getBookingDateEnd());
        }
        if (Utils.isNotNull(dto.getStatus())) {
            wrapper.eq(Bookings::getStatus, dto.getStatus());
        }
        return wrapper;
    }
}