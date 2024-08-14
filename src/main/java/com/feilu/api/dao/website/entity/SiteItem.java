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
@Table(value = "site_item")
public class SiteItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    /**
     * 产品ID
     */
    @Column(value = "productId")
    private Integer productId;

    /**
     * 站点码
     */
    @Column(value = "siteCode")
    private String siteCode;

    /**
     * 1级类目ID
     */
    @Column(value = "categoryId1")
    private Integer categoryId1;

    /**
     * 2级类目ID
     */
    @Column(value = "categoryId2")
    private Integer categoryId2;

    /**
     * 3级类目ID
     */
    @Column(value = "categoryId3")
    private Integer categoryId3;

    /**
     * 标题
     */
    private String title;

    /**
     * 主图
     */
    @Column(value = "picUrl")
    private String picUrl;

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
     * 1:引流 2:特价 3:正价 4:精品
     */
    @Column(value = "priceType")
    private Integer priceType;

    /**
     * 站点ID
     */
    @Column(value = "siteId")
    private Integer siteId;

    @Column(value = "sellItemId")
    private String sellItemId;

    /**
     * 上架状态
     */
    @Column(value = "publishStatus")
    private Integer publishStatus;

    /**
     * 是否删除
     */
    @Column(value = "deleteFlag")
    private Integer deleteFlag;

    /**
     * 是否新品
     */
    @Column(value = "newFlag")
    private Integer newFlag;

    /**
     * 是否推荐
     */
    @Column(value = "recommandFlag")
    private Integer recommandFlag;

    /**
     * 是否发布外网
     */
    @Column(value = "publicFlag")
    private Integer publicFlag;

    /**
     * 是否大码装
     */
    @Column(value = "plusSizeFlag")
    private Integer plusSizeFlag;

    /**
     * 是否老年装
     */
    @Column(value = "oldAgeFlag")
    private Integer oldAgeFlag;

    @Column(value = "itemId2")
    private String itemId2;

    @Column(value = "contentId")
    private String contentId;

    /**
     * 年龄段
     */
    @Column(value = "ageGroupFlag")
    private String ageGroupFlag;

    /**
     * 排序字段
     */
    @Column(value = "orderIdx")
    private Integer orderIdx;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
