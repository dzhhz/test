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
 * @since 2024-08-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_order_items_backup")
public class SystemOrderItemsBackup implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)@Column(value = "backupId")
    private Integer backupId;

    @Id
    private String id;

    @Column(value = "orderId")
    private String orderId;

    @Column(value = "productId")
    private Integer productId;

    @Column(value = "productSkuId")
    private Integer productSkuId;

    @Column(value = "itemId")
    private Integer itemId;

    @Column(value = "itemSkuId")
    private Integer itemSkuId;

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

    /**
     * 客服减免优惠
     */
    @Column(value = "subTotalReducePrice")
    private BigDecimal subTotalReducePrice;

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
     * 活动ID
     */
    @Column(value = "promoId")
    private Integer promoId;

    @Column(value = "workId")
    private Integer workId;

    /**
     * 活动类型
     */
    @Column(value = "promoType")
    private Integer promoType;

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
     * 系统状态
     */
    @Column(value = "sysStatus")
    private String sysStatus;

    /**
     * 订单产品的物流追踪状态
     */
    @Column(value = "trackStatus")
    private String trackStatus;

    /**
     * 逻辑删除：0没有删 1已经删除
     */
    @Column(value = "deleteFlag")
    private Integer deleteFlag;

    /**
     * 退货标记 0未退货 1全部退货 2部分退货
     */
    @Column(value = "returnFlag")
    private Integer returnFlag;

    /**
     * 待确认状态: 0未确认 1已确认
     */
    @Column(value = "confirmFlag")
    private Integer confirmFlag;

    /**
     * 备注信息
     */
    @Column(value = "customerRemark")
    private String customerRemark;

    /**
     * 签收ID
     */
    @Column(value = "signId")
    private Integer signId;

    /**
     * 售后处理类型仅做标记 1:换货 2:仅退款 3:退货退款 4:申请作废 5.客诉 6.拒收 7.转寄单 10无需退回
     */
    @Column(value = "processType")
    private Integer processType;

    /**
     * 是否需要转寄 0否 1是
     */
    @Column(value = "isExchange")
    private Integer isExchange;

    /**
     * 转寄id
     */
    @Column(value = "exchangeId")
    private Integer exchangeId;

    /**
     * 操作人
     */
    private Integer operator;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
