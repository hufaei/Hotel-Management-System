package com.sz.admin.roomtypes.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.sz.admin.hotels.pojo.po.Hotels;
import com.sz.admin.hotels.service.HotelsService;
import com.sz.admin.rooms.pojo.po.Rooms;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sz.admin.roomtypes.service.RoomTypesService;
import com.sz.admin.roomtypes.pojo.po.RoomTypes;
import com.sz.admin.roomtypes.mapper.RoomTypesMapper;
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
import java.util.List;
import java.util.function.Consumer;

import com.sz.admin.roomtypes.pojo.dto.RoomTypesCreateDTO;
import com.sz.admin.roomtypes.pojo.dto.RoomTypesUpdateDTO;
import com.sz.admin.roomtypes.pojo.dto.RoomTypesListDTO;
import com.sz.admin.roomtypes.pojo.vo.RoomTypesVO;

/**
 * <p>
 * 房型表 服务实现类
 * </p>
 *
 * @author sz-admin
 * @since 2024-12-01
 */
@Service
@RequiredArgsConstructor
public class RoomTypesServiceImpl extends ServiceImpl<RoomTypesMapper, RoomTypes> implements RoomTypesService {
    private final HotelsService hotelsService;
    @Override
    public void create(RoomTypesCreateDTO dto){
        RoomTypes roomTypes = BeanCopyUtils.copy(dto, RoomTypes.class);

        Hotels hotel = hotelsService.getById(dto.getHotelId());
        CommonResponseEnum.INVALID_ID.assertNull(hotel);

        QueryWrapper wrapper = QueryWrapper.create().eq(RoomTypes::getHotelId,dto.getHotelId())
                .and((Consumer<QueryWrapper>) qw -> qw.eq(RoomTypes::getRoomType,dto.getRoomType()));
        // 检查是否存在多条记录
        Long count = count(wrapper);
        CommonResponseEnum.INVALID.message("已存在记录").assertTrue(count >= 1);
        save(roomTypes);
    }

    @Override
    public void update(RoomTypesUpdateDTO dto){
        RoomTypes roomTypes = getById(dto.getRoomTypeId());
        CommonResponseEnum.INVALID_ID.assertNull(roomTypes);
        // photoUrl后续单独接口修改上传图片
        roomTypes.setInfo(dto.getInfo()!=null?dto.getInfo():roomTypes.getInfo());
        roomTypes.setPrice(dto.getPrice()!=null?dto.getPrice():roomTypes.getPrice());
        roomTypes.setPhotoUrls(dto.getPhotoUrls()!=null?dto.getPhotoUrls():roomTypes.getPhotoUrls());
        roomTypes.setDescription(dto.getDescription()!=null?dto.getDescription():roomTypes.getDescription());

        saveOrUpdate(roomTypes);
    }

    @Override
    public PageResult<RoomTypesVO> page(RoomTypesListDTO dto){
        Page<RoomTypesVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), RoomTypesVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<RoomTypesVO> list(RoomTypesListDTO dto){
        return listAs(buildQueryWrapper(dto), RoomTypesVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto){
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    public RoomTypesVO detail(Object id){
        RoomTypes roomTypes = getById((Serializable) id);
        CommonResponseEnum.INVALID_ID.assertNull(roomTypes);
        return BeanCopyUtils.copy(roomTypes, RoomTypesVO.class);
    }

    private static QueryWrapper buildQueryWrapper(RoomTypesListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(RoomTypes.class);
        if (Utils.isNotNull(dto.getHotelId())) {
            wrapper.eq(RoomTypes::getHotelId, dto.getHotelId());
        }
        if (Utils.isNotNull(dto.getRoomType())) {
            wrapper.eq(RoomTypes::getRoomType, dto.getRoomType());
        }
        if (Utils.isNotNull(dto.getCreatedAtStart()) && Utils.isNotNull(dto.getCreatedAtEnd())) {
            wrapper.between(RoomTypes::getCreatedAt, dto.getCreatedAtStart(), dto.getCreatedAtEnd());
        }
        return wrapper;
    }
}