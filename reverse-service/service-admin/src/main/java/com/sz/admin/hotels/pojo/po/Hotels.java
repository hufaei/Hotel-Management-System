package com.sz.admin.hotels.pojo.po;

import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.io.Serial;
import com.sz.mysql.EntityChangeListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* <p>
* 酒店信息表
* </p>
*
* @author botsuchi
* @since 2024-10-27
*/
@Data
@Table(value = "hotels", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "酒店信息表")
public class Hotels implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id()
    @Schema(description ="酒店ID")
    private String hotelId;

    @Schema(description ="酒店名称")
    private String hotelName;

    @Schema(description ="酒店英文名称")
    private String englishName;

    @Schema(description ="所属省份")
    private String province;

    @Schema(description ="所属市区")
    private String city;

    @Schema(description ="所属县")
    private String country;

    @Schema(description ="酒店星级")
    private Integer stars;

    @Schema(description ="开业年份")
    private Integer openingYear;

    @Schema(description ="装修年份")
    private Integer renovationYear;

    @Schema(description ="房间数量")
    private Integer roomCount;

    @Schema(description ="酒店地址")
    private String address;

    @Schema(description ="酒店纬度信息")
    private BigDecimal latitude;

    @Schema(description ="酒店经度信息")
    private BigDecimal longitude;

    @Schema(description ="酒店联系电话")
    private String contactPhone;

    @Schema(description ="酒店描述")
    private String description;

    @Schema(description ="交通描述")
    private String traffic;

    @Schema(description ="房型列表")
    private String roomTypeList;

    @Schema(description ="酒店添加时间")
    private LocalDateTime createdAt;

}