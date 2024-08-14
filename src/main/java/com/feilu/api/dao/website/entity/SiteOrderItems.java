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
@Table(value = "site_order_items")
public class SiteOrderItems implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(value = "orderId")
    private String orderId;

    @Column(value = "itemId")
    private Integer itemId;

    @Column(value = "itemSkuId")
    private Integer itemSkuId;

    /**
     * 货币
     */
    @Column(value = "currencyCode")
    private String currencyCode;

    @Column(value = "salePrice")
    private BigDecimal salePrice;

    @Column(value = "buyNum")
    private Integer buyNum;

    @Column(value = "skuAttrValues")
    private String skuAttrValues;

    @Column(value = "skuAttrNames")
    private String skuAttrNames;

    @Column(value = "subTotalSalePrice")
    private BigDecimal subTotalSalePrice;

    @Column(value = "subTotalShippingPrice")
    private BigDecimal subTotalShippingPrice;

    @Column(value = "subTotalPromoPrice")
    private BigDecimal subTotalPromoPrice;

    @Column(value = "subTotalCouponPrice")
    private BigDecimal subTotalCouponPrice;

    @Column(value = "subTotalPointPrice")
    private BigDecimal subTotalPointPrice;

    @Column(value = "subTotalWalletPrice")
    private BigDecimal subTotalWalletPrice;

    @Column(value = "subTotalPayPrice")
    private BigDecimal subTotalPayPrice;

    @Column(value = "isGift")
    private Integer isGift;

    private Integer status;

    /**
     * 1:h5 2:app 3:pc
     */
    @Column(value = "platformId")
    private Integer platformId;

    /**
     * 订单商品状态
     */
    @Column(value = "deviceId")
    private String deviceId;

    @Column(value = "memberId")
    private String memberId;

    /**
     * 活动类型
     */
    @Column(value = "promoType")
    private Integer promoType;

    @Column(value = "workId")
    private Integer workId;

    /**
     * spm
     */
    private String spm;

    private String s1;

    private String s2;

    private String s3;

    private String ip;

    /**
     * 售后退货状态
     */
    @Column(value = "refundStatus")
    private Integer refundStatus;

    /**
     * 逻辑删除
     */
    @Column(value = "deleteFlag")
    private Integer deleteFlag;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
