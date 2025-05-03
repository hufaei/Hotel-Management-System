package com.sz.admin.hotels.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * Hotels返回vo
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "Hotels返回vo")
public class HotelsVO {

    @ExcelIgnore
    @Schema(description =  "酒店ID")
    private String hotelId;

    @ExcelProperty(value = "酒店名称")
    @Schema(description =  "酒店名称")
    private String hotelName;

    @ExcelProperty(value = "酒店英文名称")
    @Schema(description ="酒店英文名称")
    private String englishName;

    @ExcelProperty(value = "所属省份")
    @Schema(description ="所属省份")
    private String province;

    @ExcelProperty(value = "所属市区")
    @Schema(description ="所属市区")
    private String city;

    @ExcelProperty(value = "所属县")
    @Schema(description ="所属县")
    private String country;

    @ExcelProperty(value = "酒店星级")
    @Schema(description ="酒店星级")
    private Integer stars;

    @ExcelProperty(value = "开业年份")
    @Schema(description ="开业年份")
    private Integer openingYear;

    @ExcelProperty(value = "装修年份")
    @Schema(description ="装修年份")
    private Integer renovationYear;

    @ExcelProperty(value = "房间数量")
    @Schema(description ="房间数量")
    private Integer roomCount;

    @ExcelProperty(value = "酒店地址")
    @Schema(description ="酒店地址")
    private String address;

    @ExcelProperty(value = "酒店纬度信息")
    @Schema(description ="酒店纬度信息")
    private BigDecimal latitude;

    @ExcelProperty(value = "酒店经度信息")
    @Schema(description ="酒店经度信息")
    private BigDecimal longitude;

    @ExcelProperty(value = "酒店联系电话")
    @Schema(description ="酒店联系电话")
    private String contactPhone;

    @ExcelProperty(value = "酒店描述")
    @Schema(description ="酒店描述")
    private String description;

    @ExcelProperty(value = "交通描述")
    @Schema(description ="交通描述")
    private String traffic;

    @ExcelProperty(value = "房型列表")
    @Schema(description ="房型列表")
    private String roomTypeList;

    @Schema(description ="外观照片")
    private String img;

    @ExcelProperty(value = "酒店添加时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "酒店添加时间")
    private LocalDateTime createdAt;

    @Schema(description = "起价")
    private BigDecimal minPrice;

    @Schema(description = "外显评分")
    private Double rate;

}