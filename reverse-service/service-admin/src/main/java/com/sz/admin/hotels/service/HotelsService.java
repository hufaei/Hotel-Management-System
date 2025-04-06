package com.sz.admin.hotels.service;

import com.mybatisflex.core.service.IService;
import com.sz.admin.hotels.pojo.po.Hotels;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.core.common.entity.PageResult;
import java.util.List;
import com.sz.admin.hotels.pojo.dto.HotelsCreateDTO;
import com.sz.admin.hotels.pojo.dto.HotelsUpdateDTO;
import com.sz.admin.hotels.pojo.dto.HotelsListDTO;
import com.sz.admin.hotels.pojo.vo.HotelsVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 酒店信息表 Service
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
public interface HotelsService extends IService<Hotels> {

    void create(HotelsCreateDTO dto);

    void update(HotelsUpdateDTO dto);

    PageResult<HotelsVO> page(HotelsListDTO dto);

    List<HotelsVO> list(HotelsListDTO dto);

    void remove(SelectIdsDTO dto);

    HotelsVO detail(Object id);

    void importExcel(ImportExcelDTO dto);

    void exportExcel(HotelsListDTO dto, HttpServletResponse response);

    double getAvgScore(String hotelId);
}