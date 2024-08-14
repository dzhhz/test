package com.feilu.api.common.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class EsItemResp implements Serializable{

	@Serial
	private static final long serialVersionUID = 145342849235859L;

	/**
	 * 标题
	 */
	public String title;

	/**
	 * 市场价
	 */
	public String marketPrice;

	/**
	 * 售卖价格
	 */
	public String salePrice;

	/**
	 * 市场价 txt
	 */
	public String marketPriceTxt;

	/**
	 * 售卖价格txt
	 */
	public String salePriceTxt;

	/**
	 * 折扣
	 */
	public String discountTxt;

	/**
	 * 图片url
	 */
	public String picUrl;

	/**
	 * 货币代码
	 */
	public String currencyCode;

	/**
	 * sid
	 */
	public String sid;

	/**
	 * 标签
	 */
	public String tag;

	/**
	 * 商品id
	 */
	public int itemId;

	/**
	 * 商品SKU颜色图片
	 */
	public List<EsItemSkuImage> skuImageList;

	/**
	 * 促销名称
	 */
	public String promoName;

	/**
	 * 促销类型
	 */
	public int promoType;

	/**
	 * workId
	 */
	public int workId;

	/**
	 * 1:引流 2:特价 3:正价 4:精品
	 */
	public int priceType;

	/**
	 * usdPrice
	 */
	public BigDecimal usdPrice;

	/**
	 * contentId
	 */
	public String contentId;
}
