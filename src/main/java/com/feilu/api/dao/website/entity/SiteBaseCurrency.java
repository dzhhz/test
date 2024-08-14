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
@Table(value = "site_base_currency")
public class SiteBaseCurrency implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(value = "currencyCode")
    private String currencyCode;

    @Column(value = "currencySymbol")
    private String currencySymbol;

    @Column(value = "currencyName")
    private String currencyName;

    @Column(value = "currencyNameCN")
    private String currencyNameCN;

    /**
     * 1美元等于多少其他币种
     */
    @Column(value = "fromUsdRate")
    private BigDecimal fromUsdRate;

    /**
     * 1其他币种等于多少美元
     */
    @Column(value = "toUsdRate")
    private BigDecimal toUsdRate;

    @Column(value = "currencyFormat")
    private String currencyFormat;

    /**
     * 小数位
     */
    @Column(value = "decimalNum")
    private Integer decimalNum;

    private Integer status;

    private String image;

    /**
     * 排序值
     */
    @Column(value = "orderIdx")
    private Integer orderIdx;

}
