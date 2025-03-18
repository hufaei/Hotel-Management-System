package com.sz.admin.hotelowners.service;

import com.mybatisflex.core.service.IService;
import com.sz.admin.hotelowners.pojo.po.HotelOwners;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.core.common.entity.PageResult;
import java.util.List;
import com.sz.admin.hotelowners.pojo.dto.HotelOwnersCreateDTO;
import com.sz.admin.hotelowners.pojo.dto.HotelOwnersUpdateDTO;
import com.sz.admin.hotelowners.pojo.dto.HotelOwnersListDTO;
import com.sz.admin.hotelowners.pojo.vo.HotelOwnersVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 酒店入驻代表人表 Service
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
public interface HotelOwnersService extends IService<HotelOwners> {

    void create(HotelOwnersCreateDTO dto);

    void update(HotelOwnersUpdateDTO dto);

    PageResult<HotelOwnersVO> page(HotelOwnersListDTO dto);

    List<HotelOwnersVO> list(HotelOwnersListDTO dto);

    void remove(SelectIdsDTO dto);

    HotelOwnersVO detail(Object id);

    void importExcel(ImportExcelDTO dto);

    void exportExcel(HotelOwnersListDTO dto, HttpServletResponse response);

}