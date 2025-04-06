package com.sz.admin.hotels.service;

import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.sz.admin.bookings.pojo.dto.BookingsListDTO;
import com.sz.admin.bookings.pojo.vo.BookingsVO;
import com.sz.admin.bookings.service.BookingsService;
import com.sz.admin.hotels.pojo.vo.UserProfile;
import com.sz.admin.payment.pojo.dto.PaymentListDTO;
import com.sz.admin.payment.pojo.po.Payment;
import com.sz.admin.payment.service.PaymentService;
import com.sz.platform.enums.BookingStatus;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecommendService {
    // 线程池配置
    private final PaymentService paymentService;
    private final BookingsService bookingsService;
    private final HotelsService hotelsService;
    private final RedisTemplate redisTemplate;
    // 静态线程池实例
    private static final ExecutorService pool = new ThreadPoolExecutor(
            4, // 核心线程数
            8, // 最大线程数
            60L, // 非核心线程空闲存活时间
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100), // 任务队列
            new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
    );

    public void asyncCalculateRecommend(Long userId) {
        pool.submit(() -> {
            PaymentListDTO dto = new PaymentListDTO();
            List<BookingsVO> list = new ArrayList<>();
//            Long loginId = (Long) StpUtil.getLoginId();
            Long loginId = userId;
            if (loginId != null) {
                BookingsListDTO bookingsListDTO = new BookingsListDTO();
                bookingsListDTO.setUserId(loginId);
                bookingsListDTO.setStatus(BookingStatus.FINISHED);
                list = bookingsService.list(bookingsListDTO);
            }
            List<Long> bookingIds = list.stream()
                    .map(BookingsVO::getBookingId)
                    .collect(Collectors.toList());
            // 1.获取用户最近订单
            QueryWrapper wrapper = QueryWrapper.create().from(Payment.class);
            wrapper.in("booking_id", bookingIds);
            List<Payment> orders = paymentService.list(wrapper);

            // 2.计算用户特征
            UserProfile profile = calculateProfile(orders,list);

            // 3.寻找最相似模型
            int matchedModel = findClosestModel(profile);

            // 4.存储匹配结果
            redisTemplate.opsForValue().set("recommend:user:" + userId, matchedModel);
        });

    }

    private UserProfile calculateProfile(List<Payment> orders,List<BookingsVO> list) {
        UserProfile profile = new UserProfile();
        AtomicInteger i = new AtomicInteger();
        // 省份统计（取出现最多的）
        Map<String, Long> provinceCount = orders.stream()
                .collect(Collectors.groupingBy(
                        o -> hotelsService.detail(list.get(i.getAndIncrement()).getHotelId()).getProvince(),
                        Collectors.counting()
                ));
        profile.setProvince(Collections.max(provinceCount.entrySet(), Map.Entry.comparingByValue()).getKey());
        AtomicInteger j = new AtomicInteger();
        // 平均评分
        double avgScore = orders.stream()
                .mapToDouble(o -> hotelsService.getAvgScore(list.get(j.getAndIncrement()).getHotelId()))
                .average().orElse(4.5);
        profile.setScore(avgScore);

        // 价格区间（min~max）
        DoubleSummaryStatistics stats = orders.stream()
                .mapToDouble(Payment::getAmount)
                .summaryStatistics();
        profile.setPriceRange(stats.getMin() + "-" + stats.getMax());

        return profile;
    }

    private int findClosestModel(UserProfile profile) {
        double maxSimilarity = -1;
        int bestModel = 1; // 默认第一个模型

        for (int i = 1; i <= 10; i++) {
            Map<Object, Object> model = redisTemplate.opsForHash()
                    .entries("recommend:model:" + i);

            // 简化相似度计算（余弦相似度）
            double similarity = calculateCosineSimilarity(profile, model);

            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                bestModel = i;
            }
        }
        return bestModel;
    }

    private double calculateCosineSimilarity(UserProfile profile, Map<Object, Object> model) {
        // 特征向量化示例（省份使用预设编码）
        Map<String, Integer> provinceEncoding = Map.of("浙江省", 1, "江苏省", 2); // 扩展其他省份

        // 用户向量
        double[] userVec = {
                provinceEncoding.getOrDefault(profile.getProvince(), 0),
                profile.getScore(),
                Double.parseDouble(profile.getPriceRange().split("-")[0])
        };

        // 模型向量
        double[] modelVec = {
                provinceEncoding.getOrDefault(model.get("province"), 0),
                (Double)model.get("avgScore"),
                Double.parseDouble((model.get("priceAvg")).toString())
        };

        // 余弦相似度计算
        double dotProduct = 0;
        double userNorm = 0, modelNorm = 0;
        for (int i = 0; i < userVec.length; i++) {
            dotProduct += userVec[i] * modelVec[i];
            userNorm += Math.pow(userVec[i], 2);
            modelNorm += Math.pow(modelVec[i], 2);
        }
        return dotProduct / (Math.sqrt(userNorm) * Math.sqrt(modelNorm));
    }
}