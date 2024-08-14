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
@Table(value = "tb_item_sku")
public class SystemItemSku implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(value = "siteCode")
    private String siteCode;

    @Column(value = "productId")
    private Integer productId;

    @Column(value = "productSkuId")
    private Integer productSkuId;

    /**
     * 产品ID
     */
    @Column(value = "itemId")
    private Integer itemId;

    /**
     * 属性名组合
     */
    @Column(value = "attrNamePath")
    private String attrNamePath;

    /**
     * 属性名中文组合
     */
    @Column(value = "attrNamePathCn")
    private String attrNamePathCn;

    /**
     * 属性值组合
     */
    @Column(value = "attrValuePath")
    private String attrValuePath;

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
     * 库存限制
     */
    @Column(value = "stockLimit")
    private Integer stockLimit;

    /**
     * 库存数
     */
    @Column(value = "stockNum")
    private Integer stockNum;

    private Integer status;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
