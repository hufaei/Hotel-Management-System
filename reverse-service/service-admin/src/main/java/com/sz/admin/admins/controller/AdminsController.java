package com.sz.admin.admins.controller;

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
import com.sz.admin.admins.service.AdminsService;
import com.sz.admin.admins.pojo.dto.AdminsCreateDTO;
import com.sz.admin.admins.pojo.dto.AdminsUpdateDTO;
import com.sz.admin.admins.pojo.dto.AdminsListDTO;
import com.sz.admin.admins.pojo.vo.AdminsVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 管理员信息表 Controller
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Tag(name =  "管理员信息表")
@RestController
@RequestMapping("admins")
@RequiredArgsConstructor
public class AdminsController  {

    private final AdminsService adminsService;

    @Operation(summary = "新增")
    @SaCheckPermission(value = "admins.create")
    @PostMapping
    public ApiResult create(@RequestBody AdminsCreateDTO dto) {
        adminsService.create(dto);
        return ApiResult.success();
    }

    @Operation(summary = "修改")
    @SaCheckPermission(value = "admins.update")
    @PutMapping
    public ApiResult update(@RequestBody AdminsUpdateDTO dto) {
        adminsService.update(dto);
        return ApiResult.success();
    }

    @Operation(summary = "删除")
    @SaCheckPermission(value = "admins.remove")
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        adminsService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
    @SaCheckPermission(value = "admins.query_table")
    @GetMapping
    public ApiResult<PageResult<AdminsVO>> list(AdminsListDTO dto) {
        return ApiPageResult.success(adminsService.page(dto));
    }

    @Operation(summary = "详情")
    @SaCheckPermission(value = "admins.query_table")
    @GetMapping("/{id}")
    public ApiResult<AdminsVO> detail(@PathVariable Object id) {
        return ApiResult.success(adminsService.detail(id));
    }

    @Operation(summary = "导入")
    @Parameters({
      @Parameter(name = "file", description = "上传文件", schema = @Schema(type = "string", format = "binary"), required = true),
    })
    @SaCheckPermission(value = "admins.import")
    @PostMapping("/import")
    public void importExcel(@ModelAttribute ImportExcelDTO dto) {
        adminsService.importExcel(dto);
    }

    @Operation(summary = "导出")
    @SaCheckPermission(value = "admins.export")
    @PostMapping("/export")
    public void exportExcel(@RequestBody AdminsListDTO dto, HttpServletResponse response) {
        adminsService.exportExcel(dto, response);
    }
}