package com.sz.admin.roomtypeinventory.controller;

import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryBookDTO;
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
import com.sz.admin.roomtypeinventory.service.RoomTypeInventoryService;
import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryCreateDTO;
import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryUpdateDTO;
import com.sz.admin.roomtypeinventory.pojo.dto.RoomTypeInventoryListDTO;
import com.sz.admin.roomtypeinventory.pojo.vo.RoomTypeInventoryVO;

/**
 * <p>
 * 房型预订存量 Controller
 * </p>
 *
 * @author botsuchi
 * @since 2024-12-01
 */
@Tag(name =  "房型预订存量")
@RestController
@RequestMapping("room-type-inventory")
@RequiredArgsConstructor
public class RoomTypeInventoryController  {

    private final RoomTypeInventoryService roomTypeInventoryService;

    @Operation(summary = "新增")
    @PostMapping
    public ApiResult create(@RequestBody RoomTypeInventoryCreateDTO dto) {
        roomTypeInventoryService.create(dto);
        return ApiResult.success();
    }
    @Operation(summary = "更新库存")
    @PutMapping("")
    public ApiResult update(@RequestBody RoomTypeInventoryUpdateDTO dto) {
        // todo sql语句问题+入参问题
        Boolean flag = roomTypeInventoryService.update(dto);
        return ApiResult.success(flag);
    }

    // 仅调试，不对外暴露，通过其他服务做到房间库存操作
    @Operation(summary = "预订房")
    @PutMapping("/book")
    public ApiResult bookRoom(@RequestBody RoomTypeInventoryBookDTO dto) {

        Boolean flag = roomTypeInventoryService.bookRoom(dto);
        return ApiResult.success(flag);
    }
    // 仅调试，不对外暴露，通过其他服务做到房间库存操作
    @Operation(summary = "退订房")
    @PutMapping("/cancel")
    public ApiResult cancelRoom(@RequestBody RoomTypeInventoryBookDTO dto) {
        // todo 退订参数校验——超原容量
        Boolean flag = roomTypeInventoryService.cancelRoom(dto);
        return ApiResult.success(flag);
    }

    @Operation(summary = "删除")
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        roomTypeInventoryService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
    @GetMapping
    public ApiResult<PageResult<RoomTypeInventoryVO>> list(RoomTypeInventoryListDTO dto) {
        return ApiPageResult.success(roomTypeInventoryService.page(dto));
    }

    @Operation(summary = "详情")
    @GetMapping("/{roomTypeId}")
    public ApiResult<RoomTypeInventoryVO> detail(@PathVariable Object roomTypeId) {
        return ApiResult.success(roomTypeInventoryService.detail(roomTypeId));
    }


}