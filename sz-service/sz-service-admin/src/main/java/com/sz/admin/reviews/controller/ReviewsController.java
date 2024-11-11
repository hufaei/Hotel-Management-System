package com.sz.admin.reviews.controller;

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
import com.sz.admin.reviews.service.ReviewsService;
import com.sz.admin.reviews.pojo.dto.ReviewsCreateDTO;
import com.sz.admin.reviews.pojo.dto.ReviewsUpdateDTO;
import com.sz.admin.reviews.pojo.dto.ReviewsListDTO;
import com.sz.admin.reviews.pojo.vo.ReviewsVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户评价表 Controller
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Tag(name =  "用户评价表")
@RestController
@RequestMapping("reviews")
@RequiredArgsConstructor
public class ReviewsController  {

    private final ReviewsService reviewsService;

    @Operation(summary = "新增")
    @PostMapping
    public ApiResult create(@RequestBody ReviewsCreateDTO dto) {
        reviewsService.create(dto);
        return ApiResult.success();
    }

    @Operation(summary = "修改")
    @PutMapping
    public ApiResult update(@RequestBody ReviewsUpdateDTO dto) {
        reviewsService.update(dto);
        return ApiResult.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        reviewsService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
    @SaCheckPermission(value = "reviews.query_table")
    @GetMapping
    public ApiResult<PageResult<ReviewsVO>> list(ReviewsListDTO dto) {
        return ApiPageResult.success(reviewsService.page(dto));
    }

    @Operation(summary = "详情")
    @SaCheckPermission(value = "reviews.query_table")
    @GetMapping("/{id}")
    public ApiResult<ReviewsVO> detail(@PathVariable Object id) {
        return ApiResult.success(reviewsService.detail(id));
    }

    @Operation(summary = "导入")
    @Parameters({
      @Parameter(name = "file", description = "上传文件", schema = @Schema(type = "string", format = "binary"), required = true),
    })
    @PostMapping("/import")
    public void importExcel(@ModelAttribute ImportExcelDTO dto) {
        reviewsService.importExcel(dto);
    }

    @Operation(summary = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody ReviewsListDTO dto, HttpServletResponse response) {
        reviewsService.exportExcel(dto, response);
    }
}