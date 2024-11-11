package com.sz.admin.reviews.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.sz.admin.bookings.pojo.po.Bookings;
import com.sz.admin.bookings.service.BookingsService;
import com.sz.admin.hotelowners.service.HotelOwnersService;
import com.sz.admin.users.pojo.po.Users;
import com.sz.admin.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sz.admin.reviews.service.ReviewsService;
import com.sz.admin.reviews.pojo.po.Reviews;
import com.sz.admin.reviews.mapper.ReviewsMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.query.QueryChain;
import com.sz.core.common.enums.CommonResponseEnum;
import com.sz.core.util.PageUtils;
import com.sz.core.util.BeanCopyUtils;
import com.sz.core.util.Utils;
import com.sz.core.common.entity.PageResult;
import com.sz.core.common.entity.SelectIdsDTO;
import java.io.Serializable;
import java.util.List;
import com.sz.admin.reviews.pojo.dto.ReviewsCreateDTO;
import com.sz.admin.reviews.pojo.dto.ReviewsUpdateDTO;
import com.sz.admin.reviews.pojo.dto.ReviewsListDTO;
import com.sz.admin.reviews.pojo.dto.ReviewsImportDTO;
import com.sz.core.common.entity.ImportExcelDTO;
import com.sz.excel.core.ExcelResult;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import com.sz.excel.utils.ExcelUtils;
import lombok.SneakyThrows;
import com.sz.admin.reviews.pojo.vo.ReviewsVO;

/**
 * <p>
 * 用户评价表 服务实现类
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Service
@RequiredArgsConstructor
public class ReviewsServiceImpl extends ServiceImpl<ReviewsMapper, Reviews> implements ReviewsService {

    private final UsersService usersService;
    private final BookingsService bookingsService;

    @Override
    public void create(ReviewsCreateDTO dto){
        Reviews reviews = BeanCopyUtils.copy(dto, Reviews.class);
        Long userId = 1L;
        Long bookingId = reviews.getBookingId();
        Integer rate = reviews.getRating();
        // 校验
        Users users = usersService.getById(userId);
        Bookings bookings = bookingsService.getById(bookingId);
        QueryWrapper wrapper = QueryWrapper.create().eq(Reviews::getBookingId, dto.getBookingId());
        CommonResponseEnum.NOT_EXISTS.message("不存在的用户").assertNull(users);
        CommonResponseEnum.NOT_EXISTS.message("不存在的订单").assertNull(bookings);
        CommonResponseEnum.NOT_EXISTS.message("不存在的评分范围").assertFalse(rate>=0 && rate<=5);

        CommonResponseEnum.EXISTS.message("该订单已存在评论").assertTrue(count(wrapper)>0);
        // todo sa-token中填充用户id
        reviews.setUserId(userId);
        save(reviews);
    }

    @Override
    public void update(ReviewsUpdateDTO dto){
        Reviews reviews = getById(dto.getReviewId());
        Long userId = 1L;// todo sa-token中取值
        // 校验
        CommonResponseEnum.INVALID_ID.assertNull(reviews);
        CommonResponseEnum.INVALID_ID.assertFalse(userId.equals(reviews.getUserId()));

        reviews = BeanCopyUtils.copy(dto, Reviews.class);
        saveOrUpdate(reviews);
    }

    @Override
    public PageResult<ReviewsVO> page(ReviewsListDTO dto){
        Page<ReviewsVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), ReviewsVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<ReviewsVO> list(ReviewsListDTO dto){
        return listAs(buildQueryWrapper(dto), ReviewsVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto){
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    public ReviewsVO detail(Object id){
        Reviews reviews = getById((Serializable) id);
        CommonResponseEnum.INVALID_ID.assertNull(reviews);
        return BeanCopyUtils.copy(reviews, ReviewsVO.class);
    }

    @SneakyThrows
    @Override
    public void importExcel(ImportExcelDTO dto) {
        ExcelResult<ReviewsImportDTO> excelResult = ExcelUtils.importExcel(dto.getFile().getInputStream(), ReviewsImportDTO.class, true);
        List<ReviewsImportDTO> list = excelResult.getList();
        List<String> errorList = excelResult.getErrorList();
        String analysis = excelResult.getAnalysis();
        System.out.println(" analysis : " + analysis);
        System.out.println(" isCover : " + dto.getIsCover());
    }

    @SneakyThrows
    @Override
    public void exportExcel(ReviewsListDTO dto, HttpServletResponse response) {
        List<ReviewsVO> list = list(dto);
        ServletOutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(list, "用户评价表", ReviewsVO.class, os);
    }

    private static QueryWrapper buildQueryWrapper(ReviewsListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(Reviews.class);
        if (Utils.isNotNull(dto.getBookingId())) {
            wrapper.eq(Reviews::getBookingId, dto.getBookingId());
        }
        if (Utils.isNotNull(dto.getUserId())) {
            wrapper.eq(Reviews::getUserId, dto.getUserId());
        }
        if (Utils.isNotNull(dto.getCreatedAtStart()) && Utils.isNotNull(dto.getCreatedAtEnd())) {
            wrapper.between(Reviews::getCreatedAt, dto.getCreatedAtStart(), dto.getCreatedAtEnd());
        }
        return wrapper;
    }
}