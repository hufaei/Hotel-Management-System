package com.sz.admin.users.controller;

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
import com.sz.admin.users.service.UsersService;
import com.sz.admin.users.pojo.dto.UsersCreateDTO;
import com.sz.admin.users.pojo.dto.UsersUpdateDTO;
import com.sz.admin.users.pojo.dto.UsersListDTO;
import com.sz.admin.users.pojo.vo.UsersVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户信息表 Controller
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Tag(name =  "用户信息表")
@RestController
@RequestMapping("users")
@RequiredArgsConstructor // 只于final字段构造函数
public class UsersController  {

    private final UsersService usersService;

    @Operation(summary = "新增（注册）")
    @PostMapping
    public ApiResult create(@RequestBody UsersCreateDTO dto) {
        usersService.create(dto);
        return ApiResult.success();
    }

    @Operation(summary = "修改")
    @PutMapping
    public ApiResult update(@RequestBody UsersUpdateDTO dto) {
        // 传入属性值为null则不更新此字段
        usersService.update(dto);
        return ApiResult.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        usersService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
//    @SaCheckPermission(value = "users.query_table")
    @GetMapping
    public ApiResult<PageResult<UsersVO>> list(UsersListDTO dto) {
        return ApiPageResult.success(usersService.page(dto));
    }

    @Operation(summary = "检索详情")
    @GetMapping("/{keywords}")
    public ApiResult<UsersVO> detailByPhone(@PathVariable String keywords) {
        return ApiResult.success(usersService.detailByPhoneOremail(keywords));
    }


    @Operation(summary = "导入")
    @Parameters({
      @Parameter(name = "file", description = "上传文件", schema = @Schema(type = "string", format = "binary"), required = true),
    })
    @PostMapping("/import")
    public void importExcel(@ModelAttribute ImportExcelDTO dto) {
        usersService.importExcel(dto);
    }

    @Operation(summary = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody UsersListDTO dto, HttpServletResponse response) {
        usersService.exportExcel(dto, response);
    }
}