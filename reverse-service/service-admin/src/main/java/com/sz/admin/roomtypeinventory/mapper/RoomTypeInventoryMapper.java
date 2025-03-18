package com.sz.admin.roomtypeinventory.mapper;

import com.mybatisflex.core.BaseMapper;
import com.sz.admin.roomtypeinventory.pojo.po.RoomTypeInventory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;

/**
* <p>
* 房型预订存量 Mapper 接口
* </p>
*
* @author botsuchi
* @since 2024-12-01
*/
public interface RoomTypeInventoryMapper extends BaseMapper<RoomTypeInventory> {
    /**
     * 扣减库存，条件是库存必须足够。
     * @param roomTypeId 房型ID
     * @param date 日期
     * @param bookCount 预订数量
     * @return 受影响的行数
     */
    @Update("UPDATE room_type_inventory " +
            "SET available_quantity = available_quantity - #{bookCount} " +
            "WHERE room_type_id = #{roomTypeId} AND date = #{date} AND available_quantity >= #{bookCount}")
    int reduceInventory(@Param("roomTypeId") Long roomTypeId,
                        @Param("date") LocalDate date,
                        @Param("bookCount") Long bookCount);

    /**
     * 增加库存。
     * @param roomTypeId 房型ID
     * @param date 日期
     * @param bookCount 恢复的数量
     * @return 受影响的行数
     */
    @Update("UPDATE room_type_inventory " +
            "SET available_quantity = available_quantity + #{bookCount} " +
            "WHERE room_type_id = #{roomTypeId} AND date = #{date}")
    int increaseInventory(@Param("roomTypeId") Long roomTypeId,
                          @Param("date") LocalDate date,
                          @Param("bookCount") Long bookCount);

    @Update("UPDATE room_type_inventory " +
            "SET available_quantity = LEAST(#{preset}, available_quantity + (#{preset} - preset)) ,"
             +" preset = #{preset} "+
            "WHERE room_type_id = #{roomTypeId} AND date = #{date}")
    int updateInventory(@Param("roomTypeId") Long roomTypeId,
                        @Param("date") LocalDate date,
                        @Param("preset") Long preset);

}