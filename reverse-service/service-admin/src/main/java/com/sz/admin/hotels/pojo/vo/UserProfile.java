package com.sz.admin.hotels.pojo.vo;

import lombok.Data;

// 用户画像对象
@Data
public class UserProfile {
    private String province;      // 最常访问城市（根据历史行为统计）
    private Double score;      // 偏好评分范围
    private String priceRange;
}