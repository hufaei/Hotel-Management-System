package com.sz.admin.roomtypeinventory.service;

import com.mybatisflex.core.service.IService;
import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryBookDTO;
import com.sz.admin.roomtypeinventory.pojo.po.RoomTypeInventory;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.core.common.entity.PageResult;

import java.time.LocalDate;
import java.util.List;
import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryCreateDTO;
import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryUpdateDTO;
import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryListDTO;
import com.sz.admin.roomtypeinventory.pojo.vo.RoomTypeInventoryVO;

/**
 * <p>
 * 房型预订存量 Service
 * </p>
 *
 * @author botsuchi
 * @since 2024-12-01
 */
public interface RoomTypeInventoryService extends IService<RoomTypeInventory> {

    void create(RoomTypeInventoryCreateDTO dto);

    PageResult<RoomTypeInventoryVO> page(RoomTypeInventoryListDTO dto);

    List<RoomTypeInventoryVO> list(RoomTypeInventoryListDTO dto);

    void remove(SelectIdsDTO dto);

    RoomTypeInventoryVO detail(Object id);

    Boolean update(RoomTypeInventoryUpdateDTO dto);

    Boolean bookRoom(RoomTypeInventoryBookDTO dto);

    List<Boolean> batchBookRoom(List<RoomTypeInventoryBookDTO> dtoList);

    List<Boolean> batchCancelRoom(List<RoomTypeInventoryBookDTO> dtoList);

    Boolean cancelRoom(RoomTypeInventoryBookDTO dto);
}