//package com.sz.admin.hotels.pojo.dto;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.Data;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//import com.alibaba.excel.annotation.ExcelProperty;
//import org.springframework.format.annotation.DateTimeFormat;
///**
// * <p>
// * Hotels导入DTO
// * </p>
// *
// * @author botsuchi
// * @since 2024-10-27
// */
//@Data
//@Schema(description = "Hotels导入DTO")
//public class HotelsImportDTO {
//
//    @ExcelProperty(value = "酒店名称")
//    @Schema(description =  "酒店名称")
//    private String hotelName;
//
//    @ExcelProperty(value = "酒店地址")
//    @Schema(description =  "酒店地址")
//    private String address;
//
//    @ExcelProperty(value = "酒店纬度信息")
//    @Schema(description =  "酒店纬度信息")
//    private BigDecimal latitude;
//
//    @ExcelProperty(value = "酒店经度信息")
//    @Schema(description =  "酒店经度信息")
//    private BigDecimal longitude;
//
//    @ExcelProperty(value = "酒店联系邮箱")
//    @Schema(description =  "酒店联系邮箱")
//    private String contactEmail;
//
//    @ExcelProperty(value = "酒店联系电话")
//    @Schema(description =  "酒店联系电话")
//    private String contactPhone;
//
//    @Schema(description =  "酒店添加时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createdAt;
//
//}