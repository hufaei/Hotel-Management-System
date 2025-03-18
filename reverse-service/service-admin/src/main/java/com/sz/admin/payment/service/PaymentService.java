package com.sz.admin.payment.service;

import com.mybatisflex.core.service.IService;
import com.sz.admin.payment.pojo.po.Payment;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.core.common.entity.PageResult;
import java.util.List;
import com.sz.admin.payment.pojo.dto.PaymentCreateDTO;
import com.sz.admin.payment.pojo.dto.PaymentUpdateDTO;
import com.sz.admin.payment.pojo.dto.PaymentListDTO;
import com.sz.admin.payment.pojo.vo.PaymentVO;

/**
 * <p>
 * 支付单表 Service
 * </p>
 *
 * @author botsuchi
 * @since 2024-11-23
 */
public interface PaymentService extends IService<Payment> {

    void create(PaymentCreateDTO dto);

    Boolean cancel(PaymentUpdateDTO dto);

    void finished(PaymentUpdateDTO dto);

    PageResult<PaymentVO> page(PaymentListDTO dto);

    List<PaymentVO> list(PaymentListDTO dto);

    void remove(SelectIdsDTO dto);

    PaymentVO detail(Object id);



}