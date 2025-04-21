package com.sz.admin.rooms.service;

import com.mybatisflex.core.service.IService;
import com.sz.admin.rooms.pojo.po.Rooms;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.core.common.entity.PageResult;
import java.util.List;
import com.sz.admin.rooms.pojo.dto.RoomsCreateDTO;
import com.sz.admin.rooms.pojo.dto.RoomsUpdateDTO;
import com.sz.admin.rooms.pojo.dto.RoomsListDTO;
import com.sz.admin.rooms.pojo.vo.RoomsVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 房间信息表 Service
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
public interface RoomsService extends IService<Rooms> {

    void create(RoomsCreateDTO dto);

    void toCheckIn(RoomsUpdateDTO dto);

    void toReady(RoomsUpdateDTO dto);

    void toCleaning(RoomsUpdateDTO dto);

    PageResult<RoomsVO> page(RoomsListDTO dto);

    List<RoomsVO> list(RoomsListDTO dto);

    void remove(SelectIdsDTO dto);

    RoomsVO detail(Object id);

    void importExcel(ImportExcelDTO dto);

    void exportExcel(RoomsListDTO dto, HttpServletResponse response);

    void ontoData();

}