package com.sz.admin.hotelowners.controller;

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
import com.sz.admin.hotelowners.service.HotelOwnersService;
import com.sz.admin.hotelowners.pojo.dto.HotelOwnersCreateDTO;
import com.sz.admin.hotelowners.pojo.dto.HotelOwnersUpdateDTO;
import com.sz.admin.hotelowners.pojo.dto.HotelOwnersListDTO;
import com.sz.admin.hotelowners.pojo.vo.HotelOwnersVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 酒店入驻代表人表 Controller
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Tag(name =  "酒店入驻代表人表")
@RestController
@RequestMapping("hotel-owners")
@RequiredArgsConstructor
public class HotelOwnersController  {

    private final HotelOwnersService hotelOwnersService;

    @Operation(summary = "新增")
    @PostMapping
    public ApiResult create(@RequestBody HotelOwnersCreateDTO dto) {
        hotelOwnersService.create(dto);
        return ApiResult.success();
    }

    @Operation(summary = "修改")
    @PutMapping
    public ApiResult update(@RequestBody HotelOwnersUpdateDTO dto) {
        hotelOwnersService.update(dto);
        return ApiResult.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        hotelOwnersService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
//    @SaCheckPermission(value = "hotel.owners.query_table")
    @GetMapping
    public ApiResult<PageResult<HotelOwnersVO>> list(HotelOwnersListDTO dto) {
        return ApiPageResult.success(hotelOwnersService.page(dto));
    }

    @Operation(summary = "详情")
//    @SaCheckPermission(value = "hotel.owners.query_table")
    @GetMapping("/{id}")
    public ApiResult<HotelOwnersVO> detail(@PathVariable Object id) {
        return ApiResult.success(hotelOwnersService.detail(id));
    }

    @Operation(summary = "导入")
    @Parameters({
      @Parameter(name = "file", description = "上传文件", schema = @Schema(type = "string", format = "binary"), required = true),
    })
    @PostMapping("/import")
    public void importExcel(@ModelAttribute ImportExcelDTO dto) {
        hotelOwnersService.importExcel(dto);
    }

    @Operation(summary = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody HotelOwnersListDTO dto, HttpServletResponse response) {
        hotelOwnersService.exportExcel(dto, response);
    }
}