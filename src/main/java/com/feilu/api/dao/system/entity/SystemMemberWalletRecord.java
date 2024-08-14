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
@Table(value = "tb_member_wallet_record")
public class SystemMemberWalletRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(value = "memberId")
    private String memberId;

    /**
     * 站点码
     */
    @Column(value = "siteCode")
    private String siteCode;

    /**
     * 货币
     */
    @Column(value = "currencyCode")
    private String currencyCode;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * + / -
     */
    @Column(value = "optType")
    private String optType;

    /**
     * Refund Shopping Withdraw Recharge
     */
    private String type;

    /**
     * paypal credit_card bank_card
     */
    @Column(value = "accountType")
    private String accountType;

    @Column(value = "orderId")
    private String orderId;

    @Column(value = "itemId")
    private Integer itemId;

    @Column(value = "itemSkuId")
    private Integer itemSkuId;

    /**
     * Pending Completed Cancel Unpaid
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审核人
     */
    @Column(value = "checkerId")
    private Integer checkerId;

    /**
     * 售后服务单id
     */
    @Column(value = "aftersaleId")
    private Integer aftersaleId;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
