package com.feilu.api.dao.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;

/**
 *  实体类。
 *
 * @author dzh
 * @since 2024-08-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_item")
public class SystemItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 产品ID
     */
    @Column(value = "productId")
    private Integer productId;

    /**
     * 被替换的商品Id
     */
    @Column(value = "itemReplaceId")
    private Integer itemReplaceId;

    @Column(value = "sellItemId")
    private String sellItemId;

    @Column(value = "sellItemReplaceId")
    private String sellItemReplaceId;

    @Column(value = "itemId2")
    private String itemId2;

    @Column(value = "siteCode")
    private String siteCode;

    /**
     * 市场价
     */
    @Column(value = "marketPrice")
    private BigDecimal marketPrice;

    /**
     * 销售最低价
     */
    @Column(value = "salePrice")
    private BigDecimal salePrice;

    /**
     * 1:引流 2:特价 3:正价 4:精品
     */
    @Column(value = "priceType")
    private Integer priceType;

    /**
     * 台湾超商发货限制数
     */
    @Column(value = "maxMarketNum")
    private Integer maxMarketNum;

    private Integer status;

    @Column(value = "publishStatus")
    private Integer publishStatus;

    @Column(value = "newFlag")
    private Integer newFlag;

    @Column(value = "recommandFlag")
    private Integer recommandFlag;

    @Column(value = "publicFlag")
    private Integer publicFlag;

    private Integer creator;

    private Integer editor;

    @Column(value = "syncToSite")
    private Integer syncToSite;

    @Column(value = "syncError")
    private String syncError;

    /**
     * 0未设置 1待翻译 2不用翻翻译 9已经翻译译
     */
    @Column(value = "isTranslate")
    private Integer isTranslate;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
