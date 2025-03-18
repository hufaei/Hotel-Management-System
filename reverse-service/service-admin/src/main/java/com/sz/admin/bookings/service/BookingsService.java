package com.sz.admin.bookings.service;

import com.mybatisflex.core.service.IService;
import com.sz.admin.bookings.pojo.dto.BookingsCancelDTO;
import com.sz.admin.bookings.pojo.po.Bookings;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.core.common.entity.PageResult;
import java.util.List;
import com.sz.admin.bookings.pojo.dto.BookingsCreateDTO;
import com.sz.admin.bookings.pojo.dto.BookingsUpdateDTO;
import com.sz.admin.bookings.pojo.dto.BookingsListDTO;
import com.sz.admin.bookings.pojo.vo.BookingsVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 预订信息表 Service
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
public interface BookingsService extends IService<Bookings> {

    void create(BookingsCreateDTO dto);


    PageResult<BookingsVO> page(BookingsListDTO dto);

    List<BookingsVO> list(BookingsListDTO dto);

    void remove(SelectIdsDTO dto);

    BookingsVO detail(Object id);

    void confirm(BookingsUpdateDTO dto);

    void finish(BookingsCancelDTO dto);

    void cancel(BookingsCancelDTO dto);
}