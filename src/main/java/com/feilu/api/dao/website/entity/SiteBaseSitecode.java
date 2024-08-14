package com.feilu.api.dao.website.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;

/**
 *  实体类。
 *
 * @author dzh
 * @since 2024-08-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "site_base_sitecode")
public class SiteBaseSitecode implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(value = "siteCode")
    private String siteCode;

    /**
     * 语言码
     */
    @Column(value = "languageCode")
    private String languageCode;

    /**
     * 国家码
     */
    @Column(value = "countryCode")
    private String countryCode;

    /**
     * 默认支付方式
     */
    @Column(value = "paymentId")
    private Integer paymentId;

    /**
     * 默认货币
     */
    @Column(value = "currencyCode")
    private String currencyCode;

    @Column(value = "currencySymbol")
    private String currencySymbol;

    @Column(value = "currencyName")
    private String currencyName;

    @Column(value = "currencyFormat")
    private String currencyFormat;

    @Column(value = "userNameFormat")
    private String userNameFormat;

    @Column(value = "addressFormat")
    private String addressFormat;

    @Column(value = "dateFormat")
    private String dateFormat;

    @Column(value = "dateTimeFormat")
    private String dateTimeFormat;

    @Column(value = "priceToPointRate")
    private BigDecimal priceToPointRate;

    @Column(value = "pointToPriceRate")
    private BigDecimal pointToPriceRate;

    /**
     * 1 免运费  2需满金额  3需满件数 0固定运费
     */
    @Column(value = "freeAllExpressFeeFlag")
    private Integer freeAllExpressFeeFlag;

    /**
     * 免运费须达到的金额
     */
    @Column(value = "freeExpressFeeAmount")
    private BigDecimal freeExpressFeeAmount;

    /**
     * 免运费须达到的件数
     */
    @Column(value = "freeExpressFeeNumber")
    private Integer freeExpressFeeNumber;

    /**
     * 联系链接
     */
    @Column(value = "chatLink")
    private String chatLink;

    /**
     * 链接图标
     */
    @Column(value = "chatLinkIcon")
    private String chatLinkIcon;

    /**
     * 是否有联系图标
     */
    @Column(value = "hasChat")
    private Boolean hasChat;

    /**
     * 默认5M
     */
    @Column(value = "imageMaxSize")
    private Integer imageMaxSize;

    /**
     * 是否多语言版本
     */
    @Column(value = "mutiLangFlag")
    private Integer mutiLangFlag;

    /**
     * 是否加载推荐模型
     */
    @Column(value = "loadModelFlag")
    private Integer loadModelFlag;

}
