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
@Table(value = "tb_category")
public class SystemCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 类目ID
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 父类目ID
     */
    @Column(value = "parentId")
    private Integer parentId;

    /**
     * 类目名称
     */
    @Column(value = "categoryName")
    private String categoryName;

    @Column(value = "categoryShortNameCn")
    private String categoryShortNameCn;

    @Column(value = "categoryShortNameEn")
    private String categoryShortNameEn;

    /**
     * 导出报关资料用的产品重量权重 越大代表货物越重
     */
    @Column(value = "weightProportion")
    private Integer weightProportion;

    @Column(value = "categoryGoogleCode")
    private Integer categoryGoogleCode;

    @Column(value = "categoryGoogleName")
    private String categoryGoogleName;

    @Column(value = "categoryFacebookCode")
    private Integer categoryFacebookCode;

    @Column(value = "categoryFacebookName")
    private String categoryFacebookName;

    /**
     * 类目级别
     */
    @Column(value = "categoryLevel")
    private Integer categoryLevel;

    /**
     * 图标
     */
    @Column(value = "iconUrl")
    private String iconUrl;

    /**
     * 尺码表
     */
    @Column(value = "sizechartId")
    private Integer sizechartId;

    @Column(value = "sizechartImageId")
    private Integer sizechartImageId;

    /**
     * 1-male; 2-female; 3-unisex;
     */
    private Integer gender;

    /**
     * 状态
     */
    private Integer status;

    @Column(value = "labelCode")
    private String labelCode;

    @Column(value = "productNum")
    private Integer productNum;

    /**
     * 排序
     */
    @Column(value = "orderIdx")
    private BigDecimal orderIdx;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

    @Column(value = "codeLetter")
    private String codeLetter;

    /**
     * 是否已抓取
     */
    @Column(value = "isCrawled")
    private Integer isCrawled;

    @Column(value = "isParent")
    private Integer isParent;

    /**
     * 是否超重加价 1是 0否
     */
    @Column(value = "isOverWeight")
    private Integer isOverWeight;

    /**
     * 是否能发布广告
     */
    @Column(value = "advFlag")
    private Integer advFlag;

}
