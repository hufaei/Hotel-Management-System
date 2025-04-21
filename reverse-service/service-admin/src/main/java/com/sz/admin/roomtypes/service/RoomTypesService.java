package com.sz.admin.roomtypes.service;

import com.mybatisflex.core.service.IService;
import com.sz.admin.roomtypes.pojo.po.RoomTypes;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.core.common.entity.PageResult;
import java.util.List;
import com.sz.admin.roomtypes.pojo.dto.RoomTypesCreateDTO;
import com.sz.admin.roomtypes.pojo.dto.RoomTypesUpdateDTO;
import com.sz.admin.roomtypes.pojo.dto.RoomTypesListDTO;
import com.sz.admin.roomtypes.pojo.vo.RoomTypesVO;

/**
 * <p>
 * 房型表 Service
 * </p>
 *
 * @author sz-admin
 * @since 2024-12-01
 */
public interface RoomTypesService extends IService<RoomTypes> {

    void create(RoomTypesCreateDTO dto);

    void update(RoomTypesUpdateDTO dto);

    PageResult<RoomTypesVO> page(RoomTypesListDTO dto);

    List<RoomTypesVO> list(RoomTypesListDTO dto);

    void remove(SelectIdsDTO dto);

    RoomTypesVO detail(Object id);

    List<String> getRoomTypesIdsByHotelId(String hotelId);



}