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
 * @since 2024-08-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "site_promotion_coupon_record")
public class SitePromotionCouponRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(value = "couponId")
    private Integer couponId;

    /**
     * 10限时促销 11清库存 12折扣  20优惠券 21优惠码 90满减 91满购 92满送
     */
    @Column(value = "promoType")
    private Integer promoType;

    /**
     * 优惠金额
     */
    @Column(value = "promoPrice")
    private BigDecimal promoPrice;

    /**
     * 需达到的金额
     */
    @Column(value = "fullAmount")
    private BigDecimal fullAmount;

    @Column(value = "memberId")
    private String memberId;

    /**
     * 来源订单ID
     */
    @Column(value = "fromOrderId")
    private String fromOrderId;

    /**
     * 来源商品id
     */
    @Column(value = "fromItemId")
    private Integer fromItemId;

    /**
     * 订单ID
     */
    @Column(value = "orderId")
    private String orderId;

    /**
     * 商品id
     */
    @Column(value = "itemId")
    private Integer itemId;

    /**
     * 商品skuId
     */
    @Column(value = "itemSkuId")
    private Integer itemSkuId;

    /**
     * 站点码
     */
    @Column(value = "siteCode")
    private String siteCode;

    @Column(value = "beginTime")
    private LocalDateTime beginTime;

    @Column(value = "endTime")
    private LocalDateTime endTime;

    /**
     * 1主动获取 2推送 3兑换
     */
    @Column(value = "fromType")
    private Integer fromType;

    /**
     * 平台 1pc 2h5 3app
     */
    @Column(value = "fromPlatformId")
    private Integer fromPlatformId;

    /**
     * 所用积分数
     */
    @Column(value = "pointNum")
    private Integer pointNum;

    /**
     * 0:未使用  1:过期  9:已使用
     */
    @Column(value = "useStatus")
    private Integer useStatus;

    @Column(value = "usedTime")
    private LocalDateTime usedTime;

    private Integer status;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

}
