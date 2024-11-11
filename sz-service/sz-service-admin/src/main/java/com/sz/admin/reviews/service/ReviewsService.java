package com.sz.admin.reviews.service;

import com.mybatisflex.core.service.IService;
import com.sz.admin.reviews.pojo.po.Reviews;
import com.sz.core.common.entity.SelectIdsDTO;
import com.sz.core.common.entity.PageResult;
import java.util.List;
import com.sz.admin.reviews.pojo.dto.ReviewsCreateDTO;
import com.sz.admin.reviews.pojo.dto.ReviewsUpdateDTO;
import com.sz.admin.reviews.pojo.dto.ReviewsListDTO;
import com.sz.admin.reviews.pojo.vo.ReviewsVO;
import com.sz.core.common.entity.ImportExcelDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户评价表 Service
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
public interface ReviewsService extends IService<Reviews> {

    void create(ReviewsCreateDTO dto);

    void update(ReviewsUpdateDTO dto);

    PageResult<ReviewsVO> page(ReviewsListDTO dto);

    List<ReviewsVO> list(ReviewsListDTO dto);

    void remove(SelectIdsDTO dto);

    ReviewsVO detail(Object id);

    void importExcel(ImportExcelDTO dto);

    void exportExcel(ReviewsListDTO dto, HttpServletResponse response);

}