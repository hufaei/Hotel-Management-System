package com.sz.admin.hotels.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "hotels")
@Data
public class HotelDocument {
    @Id
    private String id;
    // 合并后的分词字段：包含酒店名称和地址
    private String content;
}
