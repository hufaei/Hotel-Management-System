package com.sz.admin.rooms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import com.sz.core.common.entity.ApiPageResult;
import com.sz.core.common.entity.ApiResult;
import com.sz.core.common.constant.GlobalConstant;
import com.sz.core.common.entity.PageResult;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.admin.rooms.service.RoomsService;
import com.sz.admin.rooms.pojo.dto.RoomsCreateDTO;
import com.sz.admin.rooms.pojo.dto.RoomsUpdateDTO;
import com.sz.admin.rooms.pojo.dto.RoomsListDTO;
import com.sz.admin.rooms.pojo.vo.RoomsVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 房间信息表 Controller
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Tag(name =  "房间信息表")
@RestController
@RequestMapping("rooms")
@RequiredArgsConstructor
public class RoomsController  {

    private final RoomsService roomsService;

    @Operation(summary = "新增")
    @PostMapping
    public ApiResult create(@RequestBody RoomsCreateDTO dto) {
        roomsService.create(dto);
        return ApiResult.success();
    }

    @Operation(summary = "修改")
    @PutMapping
    public ApiResult update(@RequestBody RoomsUpdateDTO dto) {
        roomsService.update(dto);
        return ApiResult.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        roomsService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
//    @SaCheckPermission(value = "rooms.query_table")
    @GetMapping
    public ApiResult<PageResult<RoomsVO>> list(RoomsListDTO dto) {
        return ApiPageResult.success(roomsService.page(dto));
    }

    @Operation(summary = "详情")
//    @SaCheckPermission(value = "rooms.query_table")
    @GetMapping("/{id}")
    public ApiResult<RoomsVO> detail(@PathVariable Object id) {
        return ApiResult.success(roomsService.detail(id));
    }

    @Operation(summary = "导入")
    @Parameters({
      @Parameter(name = "file", description = "上传文件", schema = @Schema(type = "string", format = "binary"), required = true),
    })
    @PostMapping("/import")
    public void importExcel(@ModelAttribute ImportExcelDTO dto) {
        roomsService.importExcel(dto);
    }

    @Operation(summary = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody RoomsListDTO dto, HttpServletResponse response) {
        roomsService.exportExcel(dto, response);
    }
}