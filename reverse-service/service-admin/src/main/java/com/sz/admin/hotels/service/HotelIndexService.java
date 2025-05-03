package com.sz.admin.hotels.service;

import com.sz.admin.bookings.mapper.BookingsMapper;
import com.sz.admin.bookings.pojo.po.Bookings;
import com.sz.admin.hotels.pojo.po.Hotels;
import com.sz.admin.hotels.service.HotelsService;
import com.sz.admin.reviews.mapper.ReviewsMapper;
import com.sz.admin.reviews.pojo.po.Reviews;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HotelIndexService {

    @Autowired
    private ReviewsMapper reviewsMapper;
    @Autowired
    private BookingsMapper bookingsMapper;
    @Autowired
    private HotelsService hotelsService;

    public void index() {
        List<Bookings> bookings = bookingsMapper.selectAll();
        List<Reviews> reviewsBatch = new ArrayList<>(bookings.size());

        // —— 更丰富的评价片段 ——
        String[] intro = {
                "位置非常便利", "环境很舒适", "性价比高", "服务态度很好", "装修风格独特",
                "周边设施齐全", "交通方便", "房间整洁", "氛围温馨", "安静程度不错",
                "床品柔软舒适", "浴室格局合理", "窗外景观优美", "光线充足", "隔音效果佳",
                "前台效率高", "停车场安全", "大厅设计大气", "餐饮选择丰富", "健身房设备齐全"
        };
        String[] extras = {
                " 早餐很丰富", " 前台热情周到", " Wi-Fi 信号良好", " 空调制冷效果好",
                " 卫生间干净整洁", " 采光充足", " 音响效果不错", " 电视清晰度高",
                " 停车方便", " 枕头种类多样", "床垫支撑力好", " 淋浴水压适中",
                " 迷你吧品种齐全", " 洗漱用品品质佳", " 礼宾服务专业", " 客房打扫及时",
                " 酒店活动丰富", " 周边美食多样", " 大堂氛围温馨", " 健身房24小时开放"
        };

        Random rnd = new Random();

        // —— 1. 生成评论 ——
        for (Bookings b : bookings) {
            Reviews r = new Reviews();
            r.setBookingId(b.getBookingId());
            r.setUserId(b.getUserId());

            // 1.1 先生成一个整体 baseRating，均值设 4.5，标准差 0.3，截断于 [0.5,5.0]
            double base = clampAndRound(rnd.nextGaussian() * 0.3 + 4.3);

            // 1.2 其他四项在 base 上加小扰动（高相关），扰动标准差 0.2
            r.setRating(base);
            r.setHealthRate(clampAndRound(base + rnd.nextGaussian() * 0.2));
            r.setEnvRate(clampAndRound(base + rnd.nextGaussian() * 0.2));
            r.setServiceRate(clampAndRound(base + rnd.nextGaussian() * 0.2));
            r.setFacilitiesRate(clampAndRound(base + rnd.nextGaussian() * 0.2));

            // 1.3 随机拼评论内容
            r.setComment(buildRandomComment(intro, extras, rnd));
            r.setCreatedAt(LocalDateTime.now());

            reviewsBatch.add(r);
        }

        // —— 2. 批量插入 reviews ——
        if (!reviewsBatch.isEmpty()) {
            reviewsMapper.insertBatch(reviewsBatch, 2000);
        }

        // —— 3. 统计并更新酒店平均评分 ——
        Map<String, List<Double>> hotelRatings = new HashMap<>();
        for (int i = 0; i < bookings.size(); i++) {
            String hid = bookings.get(i).getHotelId();
            double rating = reviewsBatch.get(i).getRating();
            hotelRatings.computeIfAbsent(hid, k -> new ArrayList<>()).add(rating);
        }
        hotelRatings.forEach((hotelId, list) -> {
            double avg = list.stream().mapToDouble(d -> d).average().orElse(0.0);
            avg = Math.round(avg * 10) / 10.0;  // 一位小数

            Hotels hotel = hotelsService.getById(hotelId);
            hotel.setRate(avg);
            hotelsService.updateById(hotel);
        });

        System.out.println("✅ 生成评论数据 " + reviewsBatch.size()
                + " 条，更新酒店平均评分 " + hotelRatings.size() + " 条");
    }

    /** 截断到 [0.5,5.0] 并四舍五入到 0.5 步进 */
    private double clampAndRound(double v) {
        v = Math.max(0.5, Math.min(5.0, v));
        return Math.round(v * 2) / 2.0;
    }

    /** 随机拼接评价内容：1–2 条 intro + 1–2 条 extras */
    private String buildRandomComment(String[] intro, String[] extras, Random rnd) {
        StringBuilder sb = new StringBuilder();
        List<String> iList = new ArrayList<>(Arrays.asList(intro));
        Collections.shuffle(iList, rnd);
        int inCount = rnd.nextInt(2) + 1;
        for (int i = 0; i < inCount; i++) {
            sb.append(iList.get(i));
            if (i < inCount - 1) sb.append("，");
        }
        List<String> eList = new ArrayList<>(Arrays.asList(extras));
        Collections.shuffle(eList, rnd);
        int exCount = rnd.nextInt(2) + 1;
        for (int i = 0; i < exCount; i++) {
            sb.append(eList.get(i));
        }
        sb.append("。");
        return sb.toString();
    }
}
