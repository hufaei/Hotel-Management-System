package com.sz.admin.payment.controller;

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
import com.sz.admin.payment.service.PaymentService;
import com.sz.admin.payment.pojo.dto.PaymentCreateDTO;
import com.sz.admin.payment.pojo.dto.PaymentUpdateDTO;
import com.sz.admin.payment.pojo.dto.PaymentListDTO;
import com.sz.admin.payment.pojo.vo.PaymentVO;

/**
 * <p>
 * 支付单表 Controller
 * </p>
 *
 * @author botsuchi
 * @since 2024-11-23
 */
@Tag(name =  "支付单表")
@RestController
@RequestMapping("payment")
@RequiredArgsConstructor
public class PaymentController  {

    private final PaymentService paymentService;

    @Operation(summary = "新增")
    @PostMapping
    public ApiResult create(@RequestBody PaymentCreateDTO dto) {
        paymentService.create(dto);
        return ApiResult.success();
    }

    @Operation(summary = "完成订单")
    @PutMapping("/finish")
    public ApiResult finish(@RequestBody PaymentUpdateDTO dto) {
        paymentService.finished(dto);
        return ApiResult.success();
    }

    @Operation(summary = "取消订单")
    @PutMapping("/cancel")
    public ApiResult cancel(@RequestBody PaymentUpdateDTO dto) {
        paymentService.cancel(dto);
        return ApiResult.success();
    }
    @Operation(summary = "删除")
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        paymentService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
    @SaCheckPermission(value = "payment.query_table")
    @GetMapping
    public ApiResult<PageResult<PaymentVO>> list(PaymentListDTO dto) {
        return ApiPageResult.success(paymentService.page(dto));
    }

    @Operation(summary = "详情")
    @SaCheckPermission(value = "payment.query_table")
    @GetMapping("/{id}")
    public ApiResult<PaymentVO> detail(@PathVariable Object id) {
        return ApiResult.success(paymentService.detail(id));
    }


}