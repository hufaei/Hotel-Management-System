package com.sz.admin.hotels.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
@Data
@Schema(description = "酒店ES搜索信息表")
public class HotelEsSearch {

    @Schema(description =  "关键词")
    List<String> keywords;

    @Schema(description =  "预订开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateStart;

    @Schema(description =  "预订结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateEnd;
}
