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
@Table(value = "tb_order_aftersale")
public class SystemOrderAftersale implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 主表id
     */
    @Column(value = "mainId")
    private Integer mainId;

    /**
     * 站点码
     */
    @Column(value = "siteCode")
    private String siteCode;

    /**
     * 语言码
     */
    @Column(value = "languageCode")
    private String languageCode;

    /**
     * 货币码
     */
    @Column(value = "currencyCode")
    private String currencyCode;

    /**
     * 订单id
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
     * 用户id
     */
    @Column(value = "memberId")
    private String memberId;

    /**
     * 处理类型 0:撤销/还原 1:换货 2:仅退款 3:退货退款 4:申请作废 5:客诉
     */
    @Column(value = "processType")
    private Integer processType;

    /**
     * 图片凭证
     */
    @Column(value = "imgProof")
    private String imgProof;

    /**
     * 申请原因
     */
    @Column(value = "applyReason")
    private String applyReason;

    /**
     * 原因码
     */
    @Column(value = "reasonCode")
    private String reasonCode;

    /**
     * 申请描述
     */
    @Column(value = "applyDes")
    private String applyDes;

    /**
     * 申请数量
     */
    @Column(value = "applyNum")
    private Integer applyNum;

    /**
     * 申请金额
     */
    @Column(value = "applyAmount")
    private BigDecimal applyAmount;

    /**
     * 是否已收货 0 未收货 1已收货
     */
    @Column(value = "isReceive")
    private Integer isReceive;

    /**
     * 是否需要退货 0不需要 1需要
     */
    @Column(value = "isReturnGoods")
    private Integer isReturnGoods;

    /**
     * 实际退款金额
     */
    @Column(value = "refundAmount")
    private BigDecimal refundAmount;

    /**
     * 处理完成的图片凭证
     */
    @Column(value = "completeImgProof")
    private String completeImgProof;

    /**
     * 处理描述
     */
    @Column(value = "processDes")
    private String processDes;

    /**
     * 审核时间
     */
    @Column(value = "checkTime")
    private LocalDateTime checkTime;

    /**
     * 发货时间
     */
    @Column(value = "deliverTime")
    private LocalDateTime deliverTime;

    /**
     * 签收时间
     */
    @Column(value = "signTime")
    private LocalDateTime signTime;

    /**
     * 完成时间
     */
    @Column(value = "finishTime")
    private LocalDateTime finishTime;

    /**
     * 处理状态 0:待审核  1:已处理 -1已取消 2审核通过 -2已拒绝 3返货中 4处理中 5退货完成 6退款完成 7换货完成 10商家审核
     */
    private Integer status;

    /**
     * 创建人id  0表示会员用户
     */
    @Column(value = "createUserId")
    private Integer createUserId;

    /**
     * 审核人id
     */
    @Column(value = "checkerUserId")
    private Integer checkerUserId;

    /**
     * 修改人id
     */
    @Column(value = "updateUserId")
    private Integer updateUserId;

    /**
     * 签收人
     */
    @Column(value = "signUserId")
    private Integer signUserId;

    /**
     * 退款人
     */
    @Column(value = "refundUserId")
    private Integer refundUserId;

    /**
     * 处理之前的支付状态
     */
    @Column(value = "oriPayStatus")
    private Integer oriPayStatus;

    /**
     * 处理之前的订单状态
     */
    @Column(value = "oriOrderStatus")
    private String oriOrderStatus;

    /**
     * 处理之前的ext订单状态
     */
    @Column(value = "oriExtOrderStatus")
    private String oriExtOrderStatus;

    /**
     * 处理之前的订单商品状态
     */
    @Column(value = "oriOrderItemStatus")
    private String oriOrderItemStatus;

    /**
     * 快递单号
     */
    @Column(value = "expressNo")
    private String expressNo;

    /**
     * 快递名称
     */
    @Column(value = "expressName")
    private String expressName;

    /**
     * 快递状态
     */
    @Column(value = "expressStatus")
    private String expressStatus;

    /**
     * 换货售后发货订单号
     */
    @Column(value = "newOrderNo")
    private String newOrderNo;

    /**
     * 银行
     */
    @Column(value = "bankName")
    private String bankName;

    /**
     * 银行卡
     */
    @Column(value = "bankCard")
    private String bankCard;

    /**
     * 姓名
     */
    @Column(value = "userName")
    private String userName;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 取货地址
     */
    private String address;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
