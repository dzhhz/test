package com.feilu.api.dao.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.sql.Date;

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
@Table(value = "tb_item_ranking")
public class SystemItemRanking implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 站点
     */
    @Column(value = "siteCode")
    private String siteCode;

    /**
     * 产品id
     */
    @Column(value = "productId")
    private Integer productId;

    /**
     * itemId
     */
    @Column(value = "itemId")
    private Integer itemId;

    /**
     * 产品名称
     */
    @Column(value = "productName")
    private String productName;

    /**
     * 后端分类
     */
    @Column(value = "categoryIds")
    private String categoryIds;

    /**
     * 前端分类
     */
    @Column(value = "categoryFrontIds")
    private String categoryFrontIds;

    /**
     * 年龄分组
     */
    @Column(value = "ageGroupFlag")
    private String ageGroupFlag;

    /**
     * 浏览量
     */
    @Column(value = "viewCount")
    private Integer viewCount;

    /**
     * 加购物车数量
     */
    @Column(value = "addToCartCount")
    private Integer addToCartCount;

    /**
     * 下单量
     */
    @Column(value = "orderCount")
    private Integer orderCount;

    /**
     * 排行榜统计时间
     */
    @Column(value = "rankingDate")
    private Date rankingDate;

    /**
     * 性别标识
     */
    @Column(value = "sexFlag")
    private Integer sexFlag;

}
