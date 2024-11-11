package com.sz.admin.hotels.controller;

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
import com.sz.admin.hotels.service.HotelsService;
import com.sz.admin.hotels.pojo.dto.HotelsCreateDTO;
import com.sz.admin.hotels.pojo.dto.HotelsUpdateDTO;
import com.sz.admin.hotels.pojo.dto.HotelsListDTO;
import com.sz.admin.hotels.pojo.vo.HotelsVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

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
public class HotelsController  {

    private final HotelsService hotelsService;

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

    @Operation(summary = "导入")
    @Parameters({
      @Parameter(name = "file", description = "上传文件", schema = @Schema(type = "string", format = "binary"), required = true),
    })
    @PostMapping("/import")
    public void importExcel(@ModelAttribute ImportExcelDTO dto) {
        hotelsService.importExcel(dto);
    }

    @Operation(summary = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody HotelsListDTO dto, HttpServletResponse response) {
        hotelsService.exportExcel(dto, response);
    }
}