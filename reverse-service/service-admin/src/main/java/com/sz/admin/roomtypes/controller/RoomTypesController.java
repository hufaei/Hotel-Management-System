package com.sz.admin.roomtypes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import com.sz.core.common.entity.ApiPageResult;
import com.sz.core.common.entity.ApiResult;
import com.sz.core.common.constant.GlobalConstant;
import com.sz.core.common.entity.PageResult;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.admin.roomtypes.service.RoomTypesService;
import com.sz.admin.roomtypes.pojo.dto.RoomTypesCreateDTO;
import com.sz.admin.roomtypes.pojo.dto.RoomTypesUpdateDTO;
import com.sz.admin.roomtypes.pojo.dto.RoomTypesListDTO;
import com.sz.admin.roomtypes.pojo.vo.RoomTypesVO;

/**
 * <p>
 * 房型表 Controller
 * </p>
 *
 * @author sz-admin
 * @since 2024-12-01
 */
@Tag(name =  "房型表")
@RestController
@RequestMapping("room-types")
@RequiredArgsConstructor
public class RoomTypesController  {

    private final RoomTypesService roomTypesService;

    @Operation(summary = "新增")
    @PostMapping
    public ApiResult create(@RequestBody RoomTypesCreateDTO dto) {
        roomTypesService.create(dto);
        return ApiResult.success();
    }

    @Operation(summary = "修改")
    @PutMapping
    public ApiResult update(@RequestBody RoomTypesUpdateDTO dto) {
        roomTypesService.update(dto);
        return ApiResult.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        roomTypesService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
    @GetMapping
    public ApiResult<PageResult<RoomTypesVO>> list(RoomTypesListDTO dto) {
        return ApiPageResult.success(roomTypesService.page(dto));
    }

    @Operation(summary = "详情")
    @GetMapping("/{id}")
    public ApiResult<RoomTypesVO> detail(@PathVariable Object id) {
        return ApiResult.success(roomTypesService.detail(id));
    }


}