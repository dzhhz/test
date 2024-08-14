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
 * @since 2024-08-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "site_order_bill")
public class SiteOrderBill implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * id
     */
    @Column(value = "clientId")
    private String clientId;

    /**
     * paypal商家id
     */
    @Column(value = "businessId")
    private String businessId;

    /**
     * 站点码
     */
    @Column(value = "siteCode")
    private String siteCode;

    /**
     * 币种码
     */
    @Column(value = "currencyCode")
    private String currencyCode;

    /**
     * 订单id
     */
    @Column(value = "orderId")
    private String orderId;

    /**
     * 系统用户id
     */
    @Column(value = "memberId")
    private String memberId;

    /**
     * 支付订单Id
     */
    @Column(value = "paymentId")
    private String paymentId;

    private String token;

    /**
     * 链接失效时间
     */
    @Column(value = "expireTime")
    private LocalDateTime expireTime;

    /**
     * 抓取的id
     */
    @Column(value = "captureId")
    private String captureId;

    @Column(value = "referenceId")
    private String referenceId;

    /**
     * 付款人
     */
    @Column(value = "payerId")
    private String payerId;

    /**
     * 付款人账户
     */
    @Column(value = "payerEmail")
    private String payerEmail;

    /**
     * 姓
     */
    private String surname;

    /**
     * 名
     */
    @Column(value = "givenName")
    private String givenName;

    /**
     * 收款人
     */
    @Column(value = "receiverId")
    private String receiverId;

    /**
     * 收款人账户
     */
    @Column(value = "receiverEmail")
    private String receiverEmail;

    /**
     * 交易id
     */
    @Column(value = "transactionId")
    private String transactionId;

    /**
     * 交易方式
     */
    @Column(value = "intentType")
    private String intentType;

    /**
     * 交易费用
     */
    @Column(value = "transactionFee")
    private BigDecimal transactionFee;

    /**
     * 交易状态
     */
    @Column(value = "transactionState")
    private String transactionState;

    /**
     * 交易时间
     */
    @Column(value = "transactionTime")
    private LocalDateTime transactionTime;

    /**
     * 支付状态
     */
    @Column(value = "paymentStatus")
    private String paymentStatus;

    /**
     * 总金额
     */
    @Column(value = "totalAmount")
    private BigDecimal totalAmount;

    /**
     * 商品总金额
     */
    @Column(value = "subTotalAmount")
    private BigDecimal subTotalAmount;

    /**
     * 运费
     */
    @Column(value = "shippingAmount")
    private BigDecimal shippingAmount;

    /**
     * 税费
     */
    @Column(value = "taxAmount")
    private BigDecimal taxAmount;

    /**
     * 手续费
     */
    @Column(value = "handlAmount")
    private BigDecimal handlAmount;

    /**
     * 折扣
     */
    @Column(value = "discountAmount")
    private BigDecimal discountAmount;

    /**
     * 回调id
     */
    @Column(value = "txnId")
    private String txnId;

    /**
     * 退款id
     */
    @Column(value = "refundIds")
    private String refundIds;

    /**
     * 退款金额
     */
    @Column(value = "refundAmount")
    private BigDecimal refundAmount;

    /**
     * 支付方式
     */
    @Column(value = "payType")
    private String payType;

    /**
     * 争议id
     */
    @Column(value = "disputeId")
    private String disputeId;

    @Column(value = "adminArea1")
    private String adminArea1;

    @Column(value = "adminArea2")
    private String adminArea2;

    @Column(value = "addressLine1")
    private String addressLine1;

    @Column(value = "addressLine2")
    private String addressLine2;

    @Column(value = "postalCode")
    private String postalCode;

    /**
     * 事件id
     */
    @Column(value = "webhookId")
    private String webhookId;

    private Integer status;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
