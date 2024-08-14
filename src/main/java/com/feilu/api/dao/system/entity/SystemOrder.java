package com.feilu.api.dao.system.entity;

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
@Table(value = "tb_order")
public class SystemOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * 站点码
     */
    @Column(value = "siteCode")
    private String siteCode;

    /**
     * 站点ID
     */
    @Column(value = "siteId")
    private Integer siteId;

    /**
     * 国家
     */
    @Column(value = "countryCode")
    private String countryCode;

    /**
     * 语言码
     */
    @Column(value = "languageCode")
    private String languageCode;

    /**
     * 用于拆单
     */
    @Column(value = "originId")
    private String originId;

    /**
     * 订单号
     */
    @Column(value = "orderNo")
    private String orderNo;

    /**
     * 为避免同一订单号重复生成运单号导致无法下单 #之前为正常订单号
     */
    @Column(value = "orderExpNo")
    private String orderExpNo;

    /**
     * 货币
     */
    @Column(value = "currencyCode")
    private String currencyCode;

    @Column(value = "totalBuyNum")
    private Integer totalBuyNum;

    /**
     * 总金额
     */
    @Column(value = "totalSalePrice")
    private BigDecimal totalSalePrice;

    /**
     * 运费
     */
    @Column(value = "totalShippingPrice")
    private BigDecimal totalShippingPrice;

    /**
     * 税
     */
    @Column(value = "totalTaxPrice")
    private BigDecimal totalTaxPrice;

    /**
     * 优惠金额
     */
    @Column(value = "totalPromoPrice")
    private BigDecimal totalPromoPrice;

    /**
     * 优惠券金额
     */
    @Column(value = "totalCouponPrice")
    private BigDecimal totalCouponPrice;

    /**
     * 积分金额
     */
    @Column(value = "totalPointPrice")
    private BigDecimal totalPointPrice;

    /**
     * 客服减免金额
     */
    @Column(value = "totalReducePrice")
    private BigDecimal totalReducePrice;

    /**
     * VIP优惠金额
     */
    @Column(value = "totalVipPrice")
    private BigDecimal totalVipPrice;

    /**
     * 使用钱包金额
     */
    @Column(value = "totalWalletPrice")
    private BigDecimal totalWalletPrice;

    /**
     * 已付定金
     */
    @Column(value = "totalDepositPrice")
    private BigDecimal totalDepositPrice;

    /**
     * 实际支付金额
     */
    @Column(value = "totalPayPrice")
    private BigDecimal totalPayPrice;

    /**
     * 支付方式 1货到付款 2Paypal 3braintree的信用卡支付 4 Braintree的Paypal 5现金支付
     */
    @Column(value = "paymentType")
    private Integer paymentType;

    /**
     * 配送方式
     */
    @Column(value = "shippingType")
    private Integer shippingType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 订单状态
     */
    @Column(value = "orderStatus")
    private String orderStatus;

    /**
     * 支付状态 0默认 未支付 1:已支付 -1已退款  -2已取消  -3作废  -4超时关闭
     */
    @Column(value = "payStatus")
    private Integer payStatus;

    /**
     * 运输状态
     */
    @Column(value = "shippingStatus")
    private Integer shippingStatus;

    /**
     * 评论状态
     */
    @Column(value = "reviewStatus")
    private Integer reviewStatus;

    @Column(value = "deviceId")
    private String deviceId;

    /**
     * 下单用户
     */
    @Column(value = "memberId")
    private String memberId;

    /**
     * 1pc 2h5 3app
     */
    private Integer platform;

    @Column(value = "userIP")
    private String userIP;

    /**
     * 是否已同步
     */
    @Column(value = "isSync")
    private Integer isSync;

    /**
     * 是否是合并单 0 默认不是  1是
     */
    @Column(value = "isMerge")
    private Integer isMerge;

    /**
     * 合并单的id
     */
    @Column(value = "mergeId")
    private String mergeId;

    private Integer status;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
