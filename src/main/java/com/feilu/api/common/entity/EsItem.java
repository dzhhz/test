package com.feilu.api.common.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class EsItem implements Serializable{

	@Serial
	private static final long serialVersionUID = 1676843153657267546L;

	/**
	 * 国家代码
	 */
	private String siteCode;

	/**
	 * 语言代码
	 */
	private String languageCode;

	/**
	 * 产品sid
	 */
	private String sid;

	/**
	 * 产品id
	 */
	private int itemId;

	/**
	 * 产品多分类
	 */
	private String categoryIds;

	/**
	 * 商品标题
	 */
	private String title;

	/**
	 * 商品图片
	 */
	private String picUrl;

	/**
	 * 市场价
	 */
	private BigDecimal marketPrice;

	/**
	 * 市场价带货币符号
	 */
	private String marketPriceTxt;

	/**
	 * 市场价不带货币符号
	 */
	private String marketPriceTxt2;

	/**
	 * 销售价
	 */
	private BigDecimal salePrice;

	/**
	 * 销售价带货币符号
	 */
	private String salePriceTxt;

	/**
	 * 销售价不带货币符号
	 */
	private String salePriceTxt2;

	/**
	 * 折扣
	 */
	private String discountTxt;

	/**
	 * 是否新款
	 */
	private int newFlag;

	/**
	 * 是否推荐
	 */
	private int recommandFlag;

	/**
	 * 是否热门
	 */
	private int hotFlag;

	/**
	 * 是否大码
	 */
	private int plusSizeFlag;

	/**
	 * 是否老年装
	 */
	private int oldAgeFlag;

	/**
	 * 性别 	1:女  2:男  3:通用
	 */
	private int sexFlag;

	/**
	 * 季节	1:春   2:夏   3:秋  4:冬  5：四季通用	多个,隔开
	 */
	private String seasonFlag;

	/**
	 * 1:引流 2:特价 3:正价 4:精品
	 */
	private int priceType;

	/**
	 * 属性列表
	 */
	private List<EsItemAttr> attrsList;

	/**
	 * 商品SKU颜色图片
	 */
	private List<EsItemSkuImage> skuImageList;

	/**
	 * 活动id
	 */
	private String workId;

	/**
	 * 活动名
	 */
	private String promoName;

	/**
	 * 活动类型
	 */
	private int promoType;

	/**
	 * 标签 空格隔开
	 */
	private String tags;

	/**
	 * 搜索关键词
	 */
	private String summary;

	/**
	 * 发布状态
	 */
	private int publishStatus;

	/**
	 * 商品创建时间
	 */
	private Date gmtCreate;

	/**
	 * 排序ID
	 */
	private int orderIndex;
}
