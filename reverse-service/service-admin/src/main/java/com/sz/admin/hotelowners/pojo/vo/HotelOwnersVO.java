package com.sz.admin.hotelowners.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * HotelOwners返回vo
 * </p>
 *
 * @author botsuchi
 * @since 2024-10-27
 */
@Data
@Schema(description = "HotelOwners返回vo")
public class HotelOwnersVO {

    @ExcelIgnore
    @Schema(description =  "酒店入驻代表人ID")
    private Long ownerId;

    @ExcelProperty(value = "关联的酒店ID")
    @Schema(description =  "关联的酒店ID")
    private String hotelId;

    @ExcelProperty(value = "代表人姓名")
    @Schema(description =  "代表人姓名")
    private String name;

    @ExcelProperty(value = "代表人邮箱")
    @Schema(description =  "代表人邮箱")
    private String email;

    @ExcelProperty(value = "代表人联系电话")
    @Schema(description =  "代表人联系电话")
    private String phone;

    @ExcelProperty(value = "注册时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "注册时间")
    private LocalDateTime createdAt;


}