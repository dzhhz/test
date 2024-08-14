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
@Table(value = "tb_product")
public class SystemProduct implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(value = "categoryId")
    private Integer categoryId;

    /**
     * 分类ID
     */
    @Column(value = "categoryIds")
    private String categoryIds;

    @Column(value = "categoryNames")
    private String categoryNames;

    /**
     * 所有级别分类id
     */
    @Column(value = "categoryIdPath")
    private String categoryIdPath;

    @Column(value = "categoryGoogleCode")
    private Integer categoryGoogleCode;

    @Column(value = "categoryGoogleName")
    private String categoryGoogleName;

    @Column(value = "categoryFrontId")
    private Integer categoryFrontId;

    @Column(value = "categoryFrontIdPath")
    private String categoryFrontIdPath;

    /**
     * 标题
     */
    @Column(value = "titleCn")
    private String titleCn;

    @Column(value = "titleEn")
    private String titleEn;

    /**
     * 概要内容
     */
    private String summary;

    /**
     * 站点类型比如欧美
     */
    @Column(value = "siteTypes")
    private String siteTypes;

    /**
     * 视频地址
     */
    @Column(value = "videoUrl")
    private String videoUrl;

    /**
     * 主图
     */
    @Column(value = "picUrl")
    private String picUrl;

    /**
     * 广告图
     */
    @Column(value = "advPicUrl")
    private String advPicUrl;

    /**
     * 产品主码
     */
    @Column(value = "mainCode")
    private String mainCode;

    /**
     * 状态
     */
    private Integer status;

    @Column(value = "publishStatus")
    private Integer publishStatus;

    /**
     * 选品ID
     */
    @Column(value = "selectionId")
    private Integer selectionId;

    /**
     * 性别 1:女  2:男  3:通用
     */
    @Column(value = "sexFlag")
    private Integer sexFlag;

    /**
     * 季节 1:春   2:夏   3:秋  4:冬 5:四季通用  逗号隔开
     */
    @Column(value = "seasonFlag")
    private String seasonFlag;

    /**
     * 是否大码装
     */
    @Column(value = "plusSizeFlag")
    private Integer plusSizeFlag;

    /**
     * 是否老年装
     */
    @Column(value = "oldAgeFlag")
    private Integer oldAgeFlag;

    /**
     * 是否是从老系统迁移过来的产品 0不是 1是
     */
    @Column(value = "oldToNewFlag")
    private Integer oldToNewFlag;

    /**
     * 快递签收处是否需要测量产品重量体积 0不需要 1需要
     */
    @Column(value = "needMeasureFlag")
    private Integer needMeasureFlag;

    /**
     * 创建人
     */
    private Integer creator;

    /**
     * 修改人
     */
    private Integer editor;

    /**
     * 抓取的商品id
     */
    @Column(value = "goodsId")
    private String goodsId;

    /**
     * 抓取的商家id
     */
    @Column(value = "mallId")
    private String mallId;

    /**
     * 采购价
     */
    @Column(value = "purchasePrice")
    private BigDecimal purchasePrice;

    /**
     * 抓取的商品平台来源
     */
    private String platform;

    /**
     * 是否能发布广告
     */
    @Column(value = "advFlag")
    private Integer advFlag;

    /**
     * 货物类型 ,0,普通 ,1,敏货  ,2,重货   ,3,抛货  格式: ,1,2,
     */
    @Column(value = "goodsType")
    private String goodsType;

    /**
     * 要替换的产品ID
     */
    @Column(value = "replaceProductId")
    private Integer replaceProductId;

    /**
     * 被哪个产品ID替换
     */
    @Column(value = "replaceByProductId")
    private Integer replaceByProductId;

    @Column(value = "ageGroupFlag")
    private String ageGroupFlag;

    /**
     * 是否适合穆斯林 1适合 2不适合
     */
    @Column(value = "muslimFlag")
    private Integer muslimFlag;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
