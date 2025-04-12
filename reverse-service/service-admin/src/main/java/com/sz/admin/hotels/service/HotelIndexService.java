package com.sz.admin.hotels.service;



import com.mybatisflex.core.query.QueryWrapper;
import com.sz.admin.hotels.mapper.HotelsMapper;
import com.sz.admin.hotels.pojo.po.Hotels;
import com.sz.admin.roomtypes.mapper.RoomTypesMapper;
import com.sz.admin.roomtypes.pojo.po.RoomTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class HotelIndexService {

    @Autowired
    private HotelsMapper hotelMapper;
    @Autowired
    private RoomTypesMapper roomTypesMapper;

    /**
     * 将 hotels 的id
     */
    public void indexHotels() {
        List<Hotels> hotelsList = hotelMapper.selectAll();
        List<String> list = hotelsList.stream().map(Hotels::getHotelId).toList();
        QueryWrapper wrapper = new QueryWrapper().from(RoomTypes.class);
        List<RoomTypes> roomTypes = roomTypesMapper.selectListByQuery(wrapper.in(RoomTypes::getHotelId, list));
        // 按酒店 ID 分组
        Map<String, List<RoomTypes>> roomTypesByHotel = roomTypes.stream()
                .collect(Collectors.groupingBy(RoomTypes::getHotelId));
        // 遍历每个酒店，找到最小价格并更新
        for (Hotels hotel : hotelsList) {
            List<RoomTypes> roomTypesForHotel = roomTypesByHotel.get(hotel.getHotelId());
            if (roomTypesForHotel != null && !roomTypesForHotel.isEmpty()) {
                double minPrice = roomTypesForHotel.stream()
                        .filter(roomType -> roomType.getPrice() != null)
                        .mapToDouble(RoomTypes::getPrice)
                        .min()
                        .orElse(0);
                hotel.setMinPrice(BigDecimal.valueOf(minPrice));
                hotelMapper.update(hotel);
            }
        }


    }
}
