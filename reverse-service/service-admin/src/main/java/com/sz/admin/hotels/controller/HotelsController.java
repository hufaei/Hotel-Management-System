package com.sz.admin.hotels.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.sz.admin.hotels.mapper.HotelsMapper;
import com.sz.admin.hotels.pojo.HotelDocument;
import com.sz.admin.hotels.pojo.HotelEsSearch;
import com.sz.admin.hotels.pojo.po.Hotels;
import com.sz.admin.hotels.service.HotelIndexService;
import com.sz.admin.hotels.service.RecommendService;
import com.sz.admin.roomtypeinventory.mapper.RoomTypeInventoryMapper;
import com.sz.admin.roomtypeinventory.pojo.po.RoomTypeInventory;
import com.sz.admin.roomtypeinventory.pojo.vo.RoomTypeInventoryVO;
import com.sz.admin.roomtypes.mapper.RoomTypesMapper;
import com.sz.admin.roomtypes.service.RoomTypesService;
import com.sz.core.common.enums.CommonResponseEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.sz.core.common.entity.ApiPageResult;
import com.sz.core.common.entity.ApiResult;
import com.sz.core.common.entity.PageResult;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.admin.hotels.service.HotelsService;
import com.sz.admin.hotels.pojo.dto.HotelsCreateDTO;
import com.sz.admin.hotels.pojo.dto.HotelsUpdateDTO;
import com.sz.admin.hotels.pojo.dto.HotelsListDTO;
import com.sz.admin.hotels.pojo.vo.HotelsVO;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 酒店信息表 Controller
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Tag(name =  "酒店信息表")
@RestController
@RequestMapping("hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelsController  {

    private final HotelsService hotelsService;
    private final HotelIndexService indexService;
    private final RedisTemplate redisTemplate;
    private final RoomTypeInventoryMapper inventoryMapper;
    private final RoomTypesService roomTypesService;
    private final RecommendService recommendService;
    private final ElasticsearchOperations elasticsearchOperations;

    @Operation(summary = "新增")
    @PostMapping
    public ApiResult create(@RequestBody HotelsCreateDTO dto) {
        hotelsService.create(dto);
        return ApiResult.success();
    }

    @Operation(summary = "修改")
    @PutMapping
    public ApiResult update(@RequestBody HotelsUpdateDTO dto) {
        hotelsService.update(dto);
        return ApiResult.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        hotelsService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
    @GetMapping
    public ApiResult<PageResult<HotelsVO>> list(HotelsListDTO dto) {
        return ApiPageResult.success(hotelsService.page(dto));
    }

    @Operation(summary = "详情")
    @GetMapping("/{id}")
    public ApiResult<HotelsVO> detail(@PathVariable Object id) {
        return ApiResult.success(hotelsService.detail(id));
    }

    @Operation(summary = "获取推荐")
    @GetMapping("/getRecommend")
    public ApiResult<PageResult<String>> getRecommendHotels() {
        boolean isLogin = StpUtil.isLogin();
        List<String> recommendHotels;
        if (!isLogin) {
            recommendHotels = redisTemplate.opsForList()
                    .range("recommend:model:" + 1 + ":hotels", 0, -1);
            return ApiPageResult.success(recommendHotels);
        }
        Long userId = StpUtil.getLoginIdAsLong();
//        log.info("user get recommend, id: {}", userId);
        recommendService.asyncCalculateRecommend(userId);
        // 1.检查是否已有推荐结果
        Integer modelId = (Integer) redisTemplate.opsForValue().get("recommend:user:" + userId);
        if (modelId == null) {
            log.info("没有推荐结果");
            Random random = new Random();
            modelId = random.nextInt(10) + 1;
        }
        recommendHotels = redisTemplate.opsForList()
                .range("recommend:model:" + modelId + ":hotels", 0, -1);
        // 2.从对应模型获取推荐列表
        return ApiPageResult.success(recommendHotels);
    }
    @Operation(summary = "获取推荐")
    @PostMapping("/recommend")
    public ApiResult recommend() {
        boolean isLogin = StpUtil.isLogin();
        CommonResponseEnum.NOLOGIN.assertFalse(isLogin);

        Long userId = StpUtil.getLoginIdAsLong();
        log.info("user get recommend, id: {}", userId);
        recommendService.asyncCalculateRecommend(userId);

        return ApiPageResult.success();
    }
    @Operation(summary = "酒店收藏")
    @GetMapping("/collect/{hotelId}")
    public ApiResult collectHotel(@PathVariable String hotelId) {
        // 检查是否登录
        if (!StpUtil.isLogin()) {
            return ApiResult.error(CommonResponseEnum.NOLOGIN);
        }
        Long userId = StpUtil.getLoginIdAsLong();
        redisTemplate.opsForSet().add("collection:" + userId, hotelId);
        return ApiResult.success("收藏成功");
    }
    @Operation(summary = "酒店取消收藏")
    @GetMapping("/disCollect/{hotelId}")
    public ApiResult disCollectHotel(@PathVariable String hotelId) {
        // 检查是否登录
        if (!StpUtil.isLogin()) {
            return ApiResult.error(CommonResponseEnum.NOLOGIN);
        }
        Long userId = StpUtil.getLoginIdAsLong();
        redisTemplate.opsForSet().remove("collection:" + userId, hotelId);
        return ApiResult.success("收藏成功");
    }

    @Operation(summary = "酒店收藏列表")
    @GetMapping("/collection")
    public ApiResult<List<HotelsVO>> collectList() {
        // 检查是否登录
        CommonResponseEnum.NOLOGIN.assertTrue(!StpUtil.isLogin());
        Long userId = StpUtil.getLoginIdAsLong();
        // 从 redis 中获取用户收藏的酒店 ID 集合
        Set<String> hotelIds = redisTemplate.opsForSet().members("collection:" + userId);
        if (hotelIds == null || hotelIds.isEmpty()) {
            return ApiResult.success(Collections.emptyList());
        }
        // 使用 MybatisFlex 的 in 查询来获取收藏的酒店列表
        QueryWrapper wrapper = new QueryWrapper().from(Hotels.class);
        wrapper.in("hotel_id", hotelIds);
        return ApiResult.success(hotelsService.listAs(wrapper,HotelsVO.class));
    }


    /**
     * 刷数据专用接口
     */
    @PostMapping("/index")
    public ApiResult<String> indexHotels() {
        indexService.index();
        return ApiResult.success("booking数据ok");
    }

    /**
     * 分词查询接口，根据关键字匹配 ES 文档中的 content 字段，
     * 返回匹配到的酒店 id 列表
     */
    @PostMapping("/search")
    public List<String> searchHotels(@RequestBody HotelEsSearch esSearch) {
        // 构建布尔查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (String keyword : esSearch.getKeywords()) {
            boolQuery.must(QueryBuilders.matchQuery("content", keyword));
        }

        // 构建查询
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();
        List<String> matchedHotelIds = elasticsearchOperations
                .search(query, HotelDocument.class)
                .getSearchHits().stream()
                .map(hit -> hit.getContent().getId())
                .toList();
        // 2) date list
        LocalDate start = esSearch.getDateStart();
        LocalDate end   = esSearch.getDateEnd();
        List<String> availableHotels = new ArrayList<>();
        if(start!= null && end != null){
            List<LocalDate> stayDates = start.datesUntil(end.plusDays(1)).toList();


            // 3) for each hotel, check inventory
            for (String hotelId : matchedHotelIds) {
                // get all room‐types of this hotel
                List<String> roomTypeIds = roomTypesService.getRoomTypesIdsByHotelId(hotelId);

                boolean anyFullStayAvailable = roomTypeIds.stream().anyMatch(rtId -> {
                    QueryWrapper wrapper = QueryWrapper.create().from(RoomTypeInventory.class);
                    wrapper.eq(RoomTypeInventory::getRoomTypeId, rtId);
                    wrapper.between(RoomTypeInventory::getDate, start, end)
                            .gt(RoomTypeInventory::getAvailableQuantity, 0);
                    long count = inventoryMapper.selectCountByQuery(wrapper);
                    return count == stayDates.size();
                });
                if (anyFullStayAvailable) {
                    availableHotels.add(hotelId);
                }
            }
            return availableHotels;
        }
        return matchedHotelIds;

    }


}