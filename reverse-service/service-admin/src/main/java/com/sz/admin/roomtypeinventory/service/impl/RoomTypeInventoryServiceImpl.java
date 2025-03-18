package com.sz.admin.roomtypeinventory.service.impl;

import com.mybatisflex.core.row.BatchArgsSetter;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryBookDTO;
import com.sz.admin.roomtypes.pojo.po.RoomTypes;
import com.sz.admin.roomtypes.service.RoomTypesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sz.admin.roomtypeinventory.service.RoomTypeInventoryService;
import com.sz.admin.roomtypeinventory.pojo.po.RoomTypeInventory;
import com.sz.admin.roomtypeinventory.mapper.RoomTypeInventoryMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.sz.core.common.enums.CommonResponseEnum;
import com.sz.core.util.PageUtils;
import com.sz.core.util.BeanCopyUtils;
import com.sz.core.util.Utils;
import com.sz.core.common.entity.PageResult;
import com.sz.core.common.entity.SelectIdsDTO;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryCreateDTO;
import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryUpdateDTO;
import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryListDTO;
import com.sz.admin.roomtypeinventory.pojo.vo.RoomTypeInventoryVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 房型预订存量 服务实现类
 * </p>
 *
 * @author botsuchi
 * @since 2024-12-01
 */
@Service
@RequiredArgsConstructor
public class RoomTypeInventoryServiceImpl extends ServiceImpl<RoomTypeInventoryMapper, RoomTypeInventory> implements RoomTypeInventoryService {
    private final RoomTypesService roomTypesService;
    @Override
    public void create(RoomTypeInventoryCreateDTO dto){
        RoomTypeInventory roomTypeInventory = BeanCopyUtils.copy(dto, RoomTypeInventory.class);

        RoomTypes roomType = roomTypesService.getById(dto.getRoomTypeId());
        CommonResponseEnum.INVALID_ID.assertNull(roomType);
        // 数据重复校验
        QueryWrapper wrapper = QueryWrapper.create()
                .eq(RoomTypeInventory::getRoomTypeId, dto.getRoomTypeId()).eq(RoomTypeInventory::getDate, dto.getDate());
        CommonResponseEnum.EXISTS.message("已存在当天数据").assertTrue(count(wrapper) != 0);

        roomTypeInventory.setAvailableQuantity(dto.getPreset());
        save(roomTypeInventory);
    }
    // 修改当天库存
    @Override
    @Transactional
    public Boolean update(RoomTypeInventoryUpdateDTO dto){
        // 参数校验
        Long roomTypeId = dto.getRoomTypeId();
        LocalDate date = dto.getDate();
        Long preset = dto.getPreset();
        CommonResponseEnum.VALID_ERROR.assertTrue(preset < 0);

        QueryWrapper wrapper;
        // id有效性校验
        wrapper = QueryWrapper.create()
            .eq(RoomTypeInventory::getRoomTypeId, roomTypeId).eq(RoomTypeInventory::getDate, date);
        RoomTypeInventory inventory = getOne(wrapper);
        CommonResponseEnum.INVALID_ID.assertNull(inventory);

        Long soldCount = inventory.getPreset() - inventory.getAvailableQuantity();
        CommonResponseEnum.VALID_ERROR.message("设定数量小于已出售数量").assertTrue(soldCount > preset);
        // 更新库存
        int updatedRows = mapper.updateInventory(roomTypeId, date, preset);
        return updatedRows > 0;
    }


    /**
     * 扣减库存
     * @param dto 请求数据
     * @return 成功返回 true，失败返回 false
     */
    @Override
    @Transactional
    public Boolean bookRoom(RoomTypeInventoryBookDTO dto) {
        Long roomTypeId = dto.getRoomTypeId();
        LocalDate date = dto.getDate();
        Long bookCount = dto.getBookCount();

        // 扣减库存
        int rowsAffected = mapper.reduceInventory(roomTypeId, date, bookCount);

        // 判断是否扣减成功
        return rowsAffected > 0;
    }
//    @Override
//    public List<Boolean> batchBookRoom(List<RoomTypeInventoryBookDTO> dtoList) {
//        List<Boolean> results = new ArrayList<>();
//
//        // 批量执行时，逐个检查库存扣减
//        Db.executeBatch(dtoList, 1000, RoomTypeInventoryMapper.class, (mapper, dto) -> {
//            // 执行扣减库存操作，并判断结果
//            int rowsAffected = mapper.reduceInventory(dto.getRoomTypeId(), dto.getDate(), dto.getBookCount());
//            // 判断是否扣减成功
//            results.add(rowsAffected > 0);
//        });
//        return results; // 返回每个操作的成功与否
//    }
@Override
@Transactional
public List<Boolean> batchBookRoom(List<RoomTypeInventoryBookDTO> dtoList) {
    // 定义批量更新的 SQL
    String sql = "UPDATE room_type_inventory " +
            "SET available_quantity = available_quantity - ? " +
            "WHERE room_type_id = ? AND date = ? AND available_quantity >= ?";

    // 使用 updateBatch 方法批量执行
    int[] results = Db.updateBatch(sql, new BatchArgsSetter() {
        @Override
        public int getBatchSize() {
            return dtoList.size(); // 批量操作的总记录数
        }

        @Override
        public Object[] getSqlArgs(int index) {
            // 绑定每一批次的参数
            RoomTypeInventoryBookDTO dto = dtoList.get(index);
            return new Object[]{
                    dto.getBookCount(),
                    dto.getRoomTypeId(),
                    dto.getDate(),
                    dto.getBookCount()
            };
        }
    });
    System.out.println(Arrays.toString(results));
    // 转换执行结果
    return Arrays.stream(results).mapToObj(result -> result > 0).collect(Collectors.toList());
}
//    @Override
//    public List<Boolean> batchCancelRoom(List<RoomTypeInventoryBookDTO> dtoList) {
//        List<Boolean> results = new ArrayList<>();
//
//        // 批量执行时，逐个检查库存扣减
//        Db.executeBatch(dtoList, 1000, RoomTypeInventoryMapper.class, (mapper, dto) -> {
//            // 执行扣减库存操作，并判断结果
//            int rowsAffected = mapper.increaseInventory(dto.getRoomTypeId(), dto.getDate(), dto.getBookCount());
//            // 判断是否扣减成功
//            results.add(rowsAffected > 0);
//        });
//        return results; // 返回每个操作的成功与否
//    }
@Override
@Transactional
public List<Boolean> batchCancelRoom(List<RoomTypeInventoryBookDTO> dtoList) {
    // 定义批量更新的 SQL
    String sql = "UPDATE room_type_inventory " +
            "SET available_quantity = available_quantity + ? " +
            "WHERE room_type_id = ? AND date = ?";

    // 使用 updateBatch 方法批量执行
    int[] results = Db.updateBatch(sql, new BatchArgsSetter() {
        @Override
        public int getBatchSize() {
            return dtoList.size(); // 批量操作的总记录数
        }
        @Override
        public Object[] getSqlArgs(int index) {
            RoomTypeInventoryBookDTO dto = dtoList.get(index);
            return new Object[]{
                    dto.getBookCount(),
                    dto.getRoomTypeId(),
                    dto.getDate()
            };
        }
    });

    return Arrays.stream(results).mapToObj(result -> result > 0).collect(Collectors.toList());
}


    /**
     * 恢复库存
     * @param dto 请求数据
     * @return 成功返回 true，失败返回 false
     */
    @Override
    @Transactional
    public Boolean cancelRoom(RoomTypeInventoryBookDTO dto) {
        Long roomTypeId = dto.getRoomTypeId();
        LocalDate date = dto.getDate();
        Long bookCount = dto.getBookCount();

        // 增加库存
        int rowsAffected = mapper.increaseInventory(roomTypeId, date, bookCount);

        // 判断是否恢复成功
        return rowsAffected > 0;
    }

    @Override
    public PageResult<RoomTypeInventoryVO> page(RoomTypeInventoryListDTO dto){
        Page<RoomTypeInventoryVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), RoomTypeInventoryVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<RoomTypeInventoryVO> list(RoomTypeInventoryListDTO dto){
        return listAs(buildQueryWrapper(dto), RoomTypeInventoryVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto){
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    public RoomTypeInventoryVO detail(Object id){
        RoomTypeInventory roomTypeInventory = getById((Serializable) id);
        CommonResponseEnum.INVALID_ID.assertNull(roomTypeInventory);
        return BeanCopyUtils.copy(roomTypeInventory, RoomTypeInventoryVO.class);
    }

    private static QueryWrapper buildQueryWrapper(RoomTypeInventoryListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(RoomTypeInventory.class);
        if (Utils.isNotNull(dto.getRoomTypeId())) {
            wrapper.eq(RoomTypeInventory::getRoomTypeId, dto.getRoomTypeId());
        }
        if (Utils.isNotNull(dto.getDate())) {
            wrapper.eq(RoomTypeInventory::getDate, dto.getDate());
        }
        return wrapper;
    }
}