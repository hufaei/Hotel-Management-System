package com.sz.admin.bookings.controller;

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
import com.sz.admin.bookings.service.BookingsService;
import com.sz.admin.bookings.pojo.dto.BookingsCreateDTO;
import com.sz.admin.bookings.pojo.dto.BookingsUpdateDTO;
import com.sz.admin.bookings.pojo.dto.BookingsListDTO;
import com.sz.admin.bookings.pojo.vo.BookingsVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 预订信息表 Controller
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Tag(name =  "预订信息表")
@RestController
@RequestMapping("bookings")
@RequiredArgsConstructor
public class BookingsController  {

    private final BookingsService bookingsService;

    @Operation(summary = "新增")
    @PostMapping
    public ApiResult create(@RequestBody BookingsCreateDTO dto) {
        bookingsService.create(dto);
        return ApiResult.success();
    }

    // 酒店
    @Operation(summary = "确认")
    @PutMapping("/confirm")
    public ApiResult confirm(@RequestBody BookingsUpdateDTO dto) {
        bookingsService.confirm(dto);
        return ApiResult.success();
    }
    // 用户 || 酒店
    @Operation(summary = "取消")
    @PutMapping("/cancel")
    public ApiResult cancel(@RequestBody BookingsUpdateDTO dto) {
        bookingsService.cancel(dto);
        return ApiResult.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        bookingsService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
    @SaCheckPermission(value = "bookings.query_table")
    @GetMapping
    public ApiResult<PageResult<BookingsVO>> list(BookingsListDTO dto) {
        return ApiPageResult.success(bookingsService.page(dto));
    }

    @Operation(summary = "详情")
    @SaCheckPermission(value = "bookings.query_table")
    @GetMapping("/{id}")
    public ApiResult<BookingsVO> detail(@PathVariable Object id) {
        return ApiResult.success(bookingsService.detail(id));
    }

    @Operation(summary = "导入")
    @Parameters({
      @Parameter(name = "file", description = "上传文件", schema = @Schema(type = "string", format = "binary"), required = true),
    })
    @PostMapping("/import")
    public void importExcel(@ModelAttribute ImportExcelDTO dto) {
        bookingsService.importExcel(dto);
    }

    @Operation(summary = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody BookingsListDTO dto, HttpServletResponse response) {
        bookingsService.exportExcel(dto, response);
    }
}