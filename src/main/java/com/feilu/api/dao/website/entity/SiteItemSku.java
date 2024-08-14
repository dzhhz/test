package com.feilu.api.dao.website.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
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
@Table(value = "site_item_sku")
public class SiteItemSku implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    /**
     * 站点码
     */
    @Column(value = "siteCode")
    private String siteCode;

    @Column(value = "productId")
    private Integer productId;

    @Column(value = "productSkuId")
    private Integer productSkuId;

    @Column(value = "sellItemId")
    private String sellItemId;

    /**
     * 产品ID
     */
    @Column(value = "itemId")
    private Integer itemId;

    /**
     * 属性名组合
     */
    @Column(value = "attrNamePath")
    private String attrNamePath;

    /**
     * 属性名中文组合
     */
    @Column(value = "attrNamePathCn")
    private String attrNamePathCn;

    /**
     * 属性值组合
     */
    @Column(value = "attrValuePath")
    private String attrValuePath;

    /**
     * 市场价
     */
    @Column(value = "marketPrice")
    private BigDecimal marketPrice;

    /**
     * 销售价
     */
    @Column(value = "salePrice")
    private BigDecimal salePrice;

    /**
     * 市场价Txt
     */
    @Column(value = "marketPriceTxt")
    private String marketPriceTxt;

    /**
     * 销售价Txt
     */
    @Column(value = "salePriceTxt")
    private String salePriceTxt;

    /**
     * 折扣Txt
     */
    @Column(value = "discountTxt")
    private String discountTxt;

    /**
     * 美元价格
     */
    @Column(value = "usdPrice")
    private BigDecimal usdPrice;

    /**
     * 库存限制
     */
    @Column(value = "stockLimit")
    private Integer stockLimit;

    /**
     * 库存数
     */
    @Column(value = "stockNum")
    private Integer stockNum;

    private Integer status;

    /**
     * sku图片
     */
    private String image;

    /**
     * 长
     */
    private BigDecimal length;

    /**
     * 宽
     */
    private BigDecimal width;

    /**
     * 高
     */
    private BigDecimal height;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 打包方式
     */
    @Column(value = "packageType")
    private Integer packageType;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
