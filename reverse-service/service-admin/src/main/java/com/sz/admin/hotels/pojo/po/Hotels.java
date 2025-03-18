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

    @Id(keyType = KeyType.Auto)
    @Schema(description ="酒店ID")
    private Long hotelId;

    @Schema(description ="酒店名称")
    private String hotelName;

    @Schema(description ="酒店地址")
    private String address;

    @Schema(description ="酒店纬度信息")
    private BigDecimal latitude;

    @Schema(description ="酒店经度信息")
    private BigDecimal longitude;

    @Schema(description ="酒店联系邮箱")
    private String contactEmail;

    @Schema(description ="酒店联系电话")
    private String contactPhone;

    @Schema(description ="酒店添加时间")
    private LocalDateTime createdAt;

}