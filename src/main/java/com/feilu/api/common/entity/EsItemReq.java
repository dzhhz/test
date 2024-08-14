package com.feilu.api.common.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class EsItemReq implements Serializable{
	
	@Serial
	private static final long serialVersionUID = 1676843153657267546L;

	/**
	 * 国家代码
	 */
	private String countryCode;

	/**
	 * 语言代码
	 */
	private String languageCode;

	/**
	 * 站点代码
	 */
	private String siteCode;

	/**
	 * 货币
	 */
	private String currency;

	/**
	 * 产品sid	用于排除当前商品
	 */
	private String sid;

	/**
	 * 产品多分类, ES的数据格式是空格存储
	 */
	private String categoryIds;

	/**
	 * 商品标题
	 */
	private String title;

	/**
	 * 最低价格
	 */
	private BigDecimal minPrice;

	/**
	 * 最高价格
	 */
	private BigDecimal maxPrice;

	/**
	 * 是否新款
	 */
	private Integer newFlag;

	/**
	 * 是否推荐
	 */
	private Integer recommendFlag;

	/**
	 * 是否热门
	 */
	private Integer hotFlag;

	/**
	 * 是否大码
	 */
	private Integer plusSizeFlag;

	/**
	 * 是否老年装
	 */
	private Integer oldAgeFlag;

	/**
	 * 性别 	1:女  2:男  3:通用
	 */
	private Integer sexFlag;

	/**
	 * 季节	1:春   2:夏   3:秋  4:冬  5：四季通用	多个,隔开  ES的数据格式是空格存储
	 */
	private String seasonFlag;

	/**
	 * 年龄组标签
	 */
	private String ageGroupFlag;

	/**
	 * 1:引流 2:特价 3:正价 4:精品
	 */
	private Integer priceType;

	/**
	 * 101 	季节属性(新站点)
	 * 	131	春季
	 *	132	夏季
	 * 	133	秋季
	 * 	134	冬季
	 * 	属性列表	例:[{"attrValue":"132 133","attrKey":"101"},{"attrValue":"432","attrKey":"102"}]
	 */
	private String attrsList;

	/**
	 * 商品SKU颜色图片
	 */
	private String skuImageList;

	/**
	 * 活动id标签, ES的数据格式是空格存储
	 */
	private String activityIds;

	/**
	 * 搜索关键词
	 */
	private String keywords;

	/**
	 * 简介
	 */
	private String summary;

	/**
	 * 商品创建时间
	 */
	private Date gmtCreate;

	/**
	 * 排序
	 */
	private int sort;

	/**
	 * spm	关键词为空默认pagelist  关键词不为空默认search
	 */
	private String tag;

	/**
	 * 活动id
	 */
	private Integer workId;

	/**
	 * 活动类型
	 */
	private Integer promoType;

	/**
	 * yyyy-MM-dd HH:mm:ss 开始时间
	 */
	private String beginTime;

	/**
	 * 结束时间
	 */
	private String endTime;

	/**
	 * siteId
	 */
	private int siteId;

	/**
	 * siteUrl
	 */
	private String siteUrl;

	/**
	 * 页码数
	 */
	private int pageNumber;

	/**
	 * 分页大小
	 */
	private int pageSize;

	/**
	 * 要查询的itemId
	 */
	private List<Integer> itemIdList;

	/**
	 * 已经存在的itemId
	 */
	private List<Integer> existedItemIdList;
	
}
