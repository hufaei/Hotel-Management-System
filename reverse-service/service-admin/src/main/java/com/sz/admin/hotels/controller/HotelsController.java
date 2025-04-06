package com.sz.admin.hotels.controller;

import com.mybatisflex.core.query.QueryWrapper;
import com.sz.admin.hotels.mapper.HotelsMapper;
import com.sz.admin.hotels.pojo.po.Hotels;
import com.sz.admin.hotels.service.RecommendService;
import com.sz.admin.roomtypes.pojo.dto.RoomTypesListDTO;
import com.sz.admin.roomtypes.pojo.po.RoomTypes;
import com.sz.admin.roomtypes.pojo.vo.RoomTypesVO;
import com.sz.admin.roomtypes.service.RoomTypesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.sz.core.common.entity.ApiPageResult;
import com.sz.core.common.entity.ApiResult;
import com.sz.core.common.constant.GlobalConstant;
import com.sz.core.common.entity.PageResult;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.admin.hotels.service.HotelsService;
import com.sz.admin.hotels.pojo.dto.HotelsCreateDTO;
import com.sz.admin.hotels.pojo.dto.HotelsUpdateDTO;
import com.sz.admin.hotels.pojo.dto.HotelsListDTO;
import com.sz.admin.hotels.pojo.vo.HotelsVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;

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
    private final RedisTemplate redisTemplate;
    private final RecommendService recommendService;
    private final HotelsMapper hotelMapper; // 请确保已正确注入
    private final RoomTypesService roomTypesService;

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

    @Operation(summary = "测试")
    @GetMapping("/hotels")
    public List<String> getRecommendHotels(@RequestParam Long userId) {

        recommendService.asyncCalculateRecommend(userId);
        // 1.检查是否已有推荐结果
        Integer modelId = (Integer) redisTemplate.opsForValue().get("recommend:user:" + userId);
        if (modelId == null) {
            log.info("没有推荐结果");
            Random random = new Random();
            modelId = random.nextInt(10) + 1;
        }

        // 2.从对应模型获取推荐列表
        return redisTemplate.opsForList()
                .range("recommend:model:" + modelId + ":hotels", 0, -1);
    }
}