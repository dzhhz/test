package com.feilu.api.service.system.impl;

import cn.hutool.core.util.ArrayUtil;
import com.feilu.api.common.constant.*;
import com.feilu.api.common.entity.Ret;
import com.feilu.api.common.kit.FormatKit;
import com.feilu.api.common.service.TemplateService;
import com.feilu.api.common.utils.DateUtil;
import com.feilu.api.common.utils.EntityToMapConverter;
import com.feilu.api.common.utils.MapUtils;
import com.feilu.api.common.config.DynamicDatasourceHolder;
import com.feilu.api.dao.system.entity.*;
import com.feilu.api.dao.system.entity.SystemOrderConsignee;
import com.feilu.api.dao.system.mapper.*;
import com.feilu.api.dao.system.mapper.SystemOrderConsigneeMapper;
import com.feilu.api.dao.system.mapper.SystemOrderItemsMapper;
import com.feilu.api.dao.system.mapper.SystemOrderMapper;
import com.feilu.api.dao.website.entity.*;
import com.feilu.api.dao.website.entity.SiteItemSku;
import com.feilu.api.dao.website.entity.SiteOrder;
import com.feilu.api.dao.website.entity.SiteOrderItems;
import com.feilu.api.dao.website.mapper.*;
import com.feilu.api.dao.website.mapper.SiteItemSkuMapper;
import com.feilu.api.service.site.BaseSiteService;
import com.feilu.api.service.system.MemberService;
import com.feilu.api.service.system.OrderService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

	private static final long FLASH_TIME = 6 * 60 *60 * 1000;


	//WhatsApp订单确认模板消息站点
	private static final String[] WHATSAPP_SITE_CODES = new String[] {"MY8","ID8","MY9","ID9","ID","MY","PT2","SG8","SG9","SG"};
	//line订单确认模板消息站点
	private static final String[] LINE_SITE_CODES = new String[] {"TW8","TH8","TW9","TH9","TW","TH"};

	/**
	 * 配置文件
	 */

	@Value("${redis.queue.whatsapp.orderConfirmKey}")
	private static String WHATSAPP_ORDER_CONFIRM;

	private MemberService memberService;
	private BaseSiteService baseSiteService;
	private TemplateService templateService;
	private SystemOrderMapper systemOrderMapper;
	private SystemOrderAftersaleMapper systemOrderAftersaleMapper;
    private SiteOrderMapper siteOrderMapper;
	private SystemOrderItemsMapper systemOrderItemsMapper;
	private SiteOrderItemsMapper siteOrderItemsMapper;
	private SiteItemSkuMapper siteItemSkuMapper;
    private SystemOrderAftersaleStatusAliasMapper systemOrderAftersaleStatusAliasMapper;
    private SystemOrderExtMapper systemOrderExtMapper;
	private SystemOrderConsigneeMapper systemOrderConsigneeMapper;
    private SiteOrderConsigneeMapper siteOrderConsigneeMapper;
    private SystemOrderTrackingRecordMapper systemOrderTrackingRecordMapper;
    private SystemOrderBlacklistMapper systemOrderBlacklistMapper;
	private SystemOrderTrackingStatusMapper systemOrderTrackingStatusMapper;
    private SiteOrderBillMapper siteOrderBillMapper;
	private SystemOrderAftersaleReasonMapper systemOrderAftersaleReasonMapper;
    private SitePromotionCouponRecordMapper sitePromotionCouponRecordMapper;
	private SiteAreaThMapper siteAreaThMapper;
    private SiteAreaGrMapper siteAreaGrMapper;
    private SystemLogOrderMapper systemLogOrderMapper;
    private SystemOrderItemsBackupMapper systemOrderItemsBackupMapper;
	private JedisPool jedisPool;
	private SystemMemberWhatsappBusinessMapper systemMemberWhatsappBusinessMapper;

	@Autowired
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	@Autowired
	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}



	@Autowired
	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	@Autowired
	public void setSystemOrderMapper(SystemOrderMapper systemOrderMapper) {
		this.systemOrderMapper = systemOrderMapper;
	}

	@Autowired
	public void setSystemOrderAftersaleMapper(SystemOrderAftersaleMapper systemOrderAftersaleMapper) {
		this.systemOrderAftersaleMapper = systemOrderAftersaleMapper;
	}

	@Autowired
	public void setSiteOrderMapper(SiteOrderMapper siteOrderMapper) {
		this.siteOrderMapper = siteOrderMapper;
	}

	@Autowired
	public void setSystemOrderItemsMapper(SystemOrderItemsMapper systemOrderItemsMapper) {
		this.systemOrderItemsMapper = systemOrderItemsMapper;
	}

	@Autowired
	public void setSiteOrderItemsMapper(SiteOrderItemsMapper siteOrderItemsMapper) {
		this.siteOrderItemsMapper = siteOrderItemsMapper;
	}

	@Autowired
	public void setSiteItemSkuMapper(SiteItemSkuMapper siteItemSkuMapper) {
		this.siteItemSkuMapper = siteItemSkuMapper;
	}

	@Autowired
	public void setSystemOrderAftersaleStatusAliasMapper(SystemOrderAftersaleStatusAliasMapper systemOrderAftersaleStatusAliasMapper) {
		this.systemOrderAftersaleStatusAliasMapper = systemOrderAftersaleStatusAliasMapper;
	}

	@Autowired
	public void setSystemOrderExtMapper(SystemOrderExtMapper systemOrderExtMapper) {
		this.systemOrderExtMapper = systemOrderExtMapper;
	}

	@Autowired
	public void setSystemOrderConsigneeMapper(SystemOrderConsigneeMapper systemOrderConsigneeMapper) {
		this.systemOrderConsigneeMapper = systemOrderConsigneeMapper;
	}

	@Autowired
	public void setSiteOrderConsigneeMapper(SiteOrderConsigneeMapper siteOrderConsigneeMapper) {
		this.siteOrderConsigneeMapper = siteOrderConsigneeMapper;
	}

	@Autowired
	public void setSystemOrderTrackingRecordMapper(SystemOrderTrackingRecordMapper systemOrderTrackingRecordMapper) {
		this.systemOrderTrackingRecordMapper = systemOrderTrackingRecordMapper;
	}

	@Autowired
	public void setSystemOrderBlacklistMapper(SystemOrderBlacklistMapper systemOrderBlacklistMapper) {
		this.systemOrderBlacklistMapper = systemOrderBlacklistMapper;
	}

	@Autowired
	public void setSystemOrderTrackingStatusMapper(SystemOrderTrackingStatusMapper systemOrderTrackingStatusMapper) {
		this.systemOrderTrackingStatusMapper = systemOrderTrackingStatusMapper;
	}

	@Autowired
	public void setSiteOrderBillMapper(SiteOrderBillMapper siteOrderBillMapper) {
		this.siteOrderBillMapper = siteOrderBillMapper;
	}

	@Autowired
	public void setSystemOrderAftersaleReasonMapper(SystemOrderAftersaleReasonMapper systemOrderAftersaleReasonMapper) {
		this.systemOrderAftersaleReasonMapper = systemOrderAftersaleReasonMapper;
	}

	@Autowired
	public void setSitePromotionCouponRecordMapper(SitePromotionCouponRecordMapper sitePromotionCouponRecordMapper) {
		this.sitePromotionCouponRecordMapper = sitePromotionCouponRecordMapper;
	}

	@Autowired
	public void setSiteAreaThMapper(SiteAreaThMapper siteAreaThMapper) {
		this.siteAreaThMapper = siteAreaThMapper;
	}

	@Autowired
	public void setSiteAreaGrMapper(SiteAreaGrMapper siteAreaGrMapper) {
		this.siteAreaGrMapper = siteAreaGrMapper;
	}

	@Autowired
	public void setSystemLogOrderMapper(SystemLogOrderMapper systemLogOrderMapper) {
		this.systemLogOrderMapper = systemLogOrderMapper;
	}

	@Autowired
	public void setSystemOrderItemsBackupMapper(SystemOrderItemsBackupMapper systemOrderItemsBackupMapper) {
		this.systemOrderItemsBackupMapper = systemOrderItemsBackupMapper;
	}

	@Autowired
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	@Autowired
	public void setSystemMemberWhatsappBusinessMapper(SystemMemberWhatsappBusinessMapper systemMemberWhatsappBusinessMapper) {
		this.systemMemberWhatsappBusinessMapper = systemMemberWhatsappBusinessMapper;
	}

	/**
	 *
	 * @param siteCode
	 * @param memberId
	 * @param orderStatus
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 *
	 * 按钮  1隐藏  0显示
	 * hideOrderItemsMenu		订单详情
	 * hideAftersaleInfoMenu	售后详情
	 * hideRepurchaseMenu		重购/回购
	 * hideReviewMenu			评论
	 * hidePayMenu				支付
	 * hideCancelMenu			取消
	 * hideDeleteMenu			删除
	 * hideTrackMenu			追踪
	 */
	@Override
	public Page<Map<String, Object>> getMemberOrders(String siteCode, String memberId, String mobile, String orderStatus, int pageSize, int pageNumber) {

		Page<Map<String, Object>> result = new Page<>();
		List<Map<String, Object>> records;
		Integer count;
		boolean isReturns = "RETURNS".equals(orderStatus);

		if (isReturns) {
			// 退货查询
			records = systemOrderAftersaleMapper.pageOrderAfterSale(siteCode, memberId, mobile, pageSize, (pageNumber - 1) * pageSize);
			count = systemOrderAftersaleMapper.countOrderAfterSale(siteCode, memberId, mobile);
		} else {
			// 正常订单查询
			records = systemOrderMapper.pageOrders(siteCode, memberId, mobile, orderStatus, pageSize, (pageNumber - 1) * pageSize);
			count = systemOrderMapper.countOrders(siteCode, memberId, mobile, orderStatus);
		}

		// 构建分页结果
		result.setPageNumber(pageNumber);
		result.setPageSize(pageSize);
		result.setTotalRow(count);
		result.setTotalPage((int) Math.ceil((double) count / pageSize));
		result.setRecords(records);

		// 处理合并订单
		if (orderStatus == null || "ALL".equals(orderStatus)) {
			if (pageNumber == 1) {
				handleUnSyncedOrders(result, siteCode, memberId);
			}
		}

		// 更新记录信息
		for (Map<String, Object> record : result.getRecords()) {
			updateRecordInfo(record, siteCode, orderStatus);
		}

		return result;
	}

	private void handleUnSyncedOrders(Page<Map<String, Object>> result, String siteCode, String memberId) {
		DynamicDatasourceHolder.setDataSource("site");
		try {
			List<SiteOrder> siteOrders = siteOrderMapper.selectListByQuery(new QueryWrapper()
					.eq("memberId", memberId)
					.eq("siteCode", siteCode)
					.eq("isSync", 0)
					.eq("status", 1)
					.orderBy("id desc"));

			if (!siteOrders.isEmpty()) {
				List<Map<String, Object>> maps = result.getRecords();
				Set<Map<String, Object>> mapSet = new HashSet<>(maps);
				for (SiteOrder siteOrder : siteOrders) {
					Map<String, Object> orderMap = convertSiteOrderToMap(siteOrder);
					mapSet.add(orderMap);
				}

				// 更新记录
				result.setRecords(new ArrayList<>(mapSet));
				result.getRecords().sort((o1, o2) -> {
					LocalDateTime date1 = (LocalDateTime) o1.get("gmtCreate");
					LocalDateTime date2 = (LocalDateTime) o2.get("gmtCreate");
					return date2.compareTo(date1);  // 降序排序
				});
			}
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	private Map<String, Object> convertSiteOrderToMap(SiteOrder siteOrder) {
		if (siteOrder.getPaymentType() != null) {
			siteOrder.setOrderStatus(
					switch (siteOrder.getPaymentType()) {
						case 0 -> OrderStatus.ORDER_STATUS_UNPAID;
						case 1 -> OrderStatus.ORDER_STATUS_PAID;
						case -2 -> OrderStatus.ORDER_STATUS_CANCELED;
						case -3 -> OrderStatus.ORDER_STATUS_CANCEL;
						default -> OrderStatus.ORDER_STATUS_SUBMITED;
					});
		}
		return EntityToMapConverter.convertToMap(siteOrder);
	}

	private void updateRecordInfo(Map<String, Object> record, String siteCode, String orderStatus) {
		if (record.get("mergeId") != null) {
			SystemOrder systemOrder = systemOrderMapper.selectOneById(MapUtils.getStringValue(record, "mergeId"));
			if (systemOrder != null && StringUtils.isNotBlank(systemOrder.getOrderStatus())) {
				record.put("orderStatus", systemOrder.getOrderStatus());
			}
		}

		SiteBaseSitecode baseSite = baseSiteService.getBaseSite(siteCode);
		String languageCode = baseSite.getLanguageCode();
		int isAftersale = "RETURNS".equals(orderStatus) ? 1 : 0;

		record.put("hideOrderItemsMenu", isAftersale == 1 ? 0 : 1);
		record.put("hideAftersaleInfoMenu", isAftersale == 1 ? 1 : 0);

		List<Map<String, Object>> itemList = getOrderItemList(siteCode, languageCode, MapUtils.getStringValue(record, "id"), MapUtils.getStringValue(record, "orderId"), MapUtils.getIntegerValue(record, "itemId"), MapUtils.getIntegerValue(record, "itemSkuId"), isAftersale);
		record.put("itemList", itemList);

		if (isAftersale == 1) {
			record.put("orderStatusTxt", getAftersaleStatus(languageCode, MapUtils.getIntegerValue(record, "processType"), MapUtils.getIntegerValue(record, "status")));
		} else {
			record.put("orderStatusTxt", FormatKit.getOrderStatusTxt(siteCode, MapUtils.getStringValue(record, "orderStatus")));
		}

		record.put("totalPayPriceTxt", FormatKit.getCurrencyPriceTxt(MapUtils.getStringValue(record, "currencyCode"), MapUtils.getBigDecimalValue(record, "totalPayPrice")));
		handleMenuVisibility(record, orderStatus);
	}

	private void handleMenuVisibility(Map<String, Object> record, String orderStatus) {
		Integer payStatus = MapUtils.getIntValue(record, "payStatus");
		String orderStatusStr = MapUtils.getStringValue(record, "orderStatus");

		if ("RETURNS".equals(orderStatus)) {
			record.put("hideReviewMenu", 1);
			record.put("hidePayMenu", 1);
			record.put("hideTrackMenu", 1);
			record.put("hideCancelMenu", 1);
			record.put("leftSeconds", 0);
			record.put("hideDeleteMenu", payStatus < 0 ? 0 : 1);
		} else {
			record.put("hideReviewMenu", OrderStatus.ORDER_STATUS_RECEIVED.equals(orderStatusStr) && MapUtils.getIntValue(record, "reviewStatus") == 0 ? 0 : 1);
			record.put("hideDeleteMenu", payStatus < 0 ? 0 : 1);
			record.put("hideTrackMenu", payStatus < 0 ? 1 : 0);
			record.put("hidePayMenu", handlePayMenuVisibility(record, orderStatusStr));
		}
	}

	private int handlePayMenuVisibility(Map<String, Object> record, String orderStatusStr) {
		if (OrderStatus.ORDER_STATUS_UNPAID.equals(orderStatusStr)) {
			int leftSeconds = DateUtil.getLeftSeconds((LocalDateTime) record.get("gmtCreate"), FLASH_TIME);
			if (leftSeconds > 0) {
				record.put("leftSeconds", leftSeconds);
				return 0;
			} else {
				record.put("leftSeconds", 0);
				return 1;
			}
		} else {
			record.put("leftSeconds", 0);
			if (OrderStatus.ORDER_STATUS_STOCKING.equals(orderStatusStr)) {
				return MapUtils.getIntValue(record, "payStatus") == 0 ? 0 : 1;
			} else {
				return 1;
			}
		}
	}



	/**
	 *
	 * 按钮  1隐藏  0显示
	 * hideCancelMenu	取消订单
	 * hideReviewMenu	评论
	 * hideDeleteMenu	删除订单
	 * hidePayMenu		支付
	 * hideTrackMenu	追踪
	 */
	@Override
	public Map<String, Object> getMemberOrderById(String siteCode, String memberId, String mobile, String orderId) {
		SystemOrder systemOrder = fetchSystemOrder(siteCode, memberId, mobile, orderId);
		if (systemOrder == null) {
			return Collections.emptyMap(); // 返回空的 map 代替 null
		}

		Map<String, Object> orderObj = EntityToMapConverter.convertToMap(systemOrder);
		updateOrderStatus(orderObj, systemOrder);
		addPaymentDetails(orderObj, siteCode, systemOrder);
		addPriceDetails(orderObj, systemOrder);
		configureMenuVisibility(orderObj, systemOrder);
		addAdditionalInfo(orderObj, systemOrder, siteCode, orderId);

		return orderObj;
	}

	private SystemOrder fetchSystemOrder(String siteCode, String memberId, String mobile, String orderId) {
		if (StringUtils.isBlank(mobile)) {
			return systemOrderMapper.selectOneByQuery(new QueryWrapper().eq("id", orderId).eq("memberId", memberId));
		} else {
			return systemOrderMapper.selectOrderWithMobile(orderId, mobile, memberId);
		}
	}

	private void updateOrderStatus(Map<String, Object> orderObj, SystemOrder systemOrder) {
		if (StringUtils.isNotBlank(systemOrder.getMemberId())) {
			SystemOrder orderStatus = systemOrderMapper.selectOneById(systemOrder.getMergeId());
			if (orderStatus != null && StringUtils.isNotBlank(orderStatus.getOrderStatus())) {
				orderObj.put("orderStatus", orderStatus.getOrderStatus());
			}
		}
	}

	private void addPaymentDetails(Map<String, Object> orderObj, String siteCode, SystemOrder systemOrder) {
		orderObj.put("paymentId", systemOrder.getPaymentType());
		orderObj.put("paymentTypeTxt", FormatKit.getPaymentTypeTxt(siteCode, systemOrder.getPaymentType()));
		orderObj.put("orderStatusTxt", FormatKit.getOrderStatusTxt(siteCode, systemOrder.getOrderStatus()));
	}

	private void addPriceDetails(Map<String, Object> orderObj, SystemOrder systemOrder) {
		orderObj.put("totalSalePriceTxt", FormatKit.getCurrencyPriceTxt(systemOrder.getCurrencyCode(), systemOrder.getTotalSalePrice()));
		orderObj.put("totalPayPriceTxt", FormatKit.getCurrencyPriceTxt(systemOrder.getCurrencyCode(), systemOrder.getTotalPayPrice()));
		orderObj.put("totalShippingPriceTxt", FormatKit.getCurrencyPriceTxt(systemOrder.getCurrencyCode(), systemOrder.getTotalShippingPrice()));
		orderObj.put("totalTaxPriceTxt", FormatKit.getCurrencyPriceTxt(systemOrder.getCurrencyCode(), systemOrder.getTotalTaxPrice()));
		orderObj.put("totalPromoPriceTxt", formatPrice(systemOrder.getCurrencyCode(), systemOrder.getTotalPromoPrice()));
		orderObj.put("totalCouponPriceTxt", formatPrice(systemOrder.getCurrencyCode(), systemOrder.getTotalCouponPrice()));
		orderObj.put("totalPointPriceTxt", formatPrice(systemOrder.getCurrencyCode(), systemOrder.getTotalPointPrice()));
		orderObj.put("totalWalletPriceTxt", formatPrice(systemOrder.getCurrencyCode(), systemOrder.getTotalWalletPrice()));
	}

	private String formatPrice(String currencyCode, BigDecimal amount) {
		return amount.compareTo(BigDecimal.ZERO) > 0 ? "-" + FormatKit.getCurrencyPriceTxt(currencyCode, amount) : "";
	}

	private void configureMenuVisibility(Map<String, Object> orderObj, SystemOrder systemOrder) {
		orderObj.put("hideReviewMenu", shouldHideReviewMenu(systemOrder));
		orderObj.put("hideDeleteMenu", shouldHideDeleteMenu(systemOrder));
		orderObj.put("hideTrackMenu", shouldHideTrackMenu(systemOrder));
		orderObj.put("hideCancelMenu", shouldHideCancelMenu(systemOrder));
		orderObj.put("hidePayMenu", shouldHidePayMenu(systemOrder));
		orderObj.put("leftSeconds", calculateLeftSeconds(systemOrder));
	}

	private int shouldHideReviewMenu(SystemOrder systemOrder) {
		return StringUtils.equals(systemOrder.getOrderStatus(), OrderStatus.ORDER_STATUS_RECEIVED) && systemOrder.getReviewStatus() == 0 ? 0 : 1;
	}

	private int shouldHideDeleteMenu(SystemOrder systemOrder) {
		return systemOrder.getPayStatus() < 0 ? 0 : 1;
	}

	private int shouldHideTrackMenu(SystemOrder systemOrder) {
		return systemOrder.getPayStatus() < 0 ? 1 : 0;
	}

	private int shouldHideCancelMenu(SystemOrder systemOrder) {
		if (StringUtils.equals(systemOrder.getOrderStatus(), OrderStatus.ORDER_STATUS_UNPAID)) {
			return calculateLeftSeconds(systemOrder) > 0 ? 0 : 1;
		} else if (StringUtils.equals(systemOrder.getOrderStatus(), OrderStatus.ORDER_STATUS_STOCKING)) {
			return systemOrder.getPayStatus() == 0 ? 0 : 1;
		} else {
			return 1;
		}
	}

	private int shouldHidePayMenu(SystemOrder systemOrder) {
		return StringUtils.equals(systemOrder.getOrderStatus(), OrderStatus.ORDER_STATUS_UNPAID) ? (calculateLeftSeconds(systemOrder) > 0 ? 0 : 1) : 1;
	}

	private int calculateLeftSeconds(SystemOrder systemOrder) {
		if (StringUtils.equals(systemOrder.getOrderStatus(), OrderStatus.ORDER_STATUS_UNPAID)) {
			return DateUtil.getLeftSeconds(systemOrder.getGmtCreate(), FLASH_TIME);
		}
		return 0;
	}

	private void addAdditionalInfo(Map<String, Object> orderObj, SystemOrder systemOrder, String siteCode, String orderId) {
		orderObj.put("hideOrderItemsMenu", 1);
		orderObj.put("hideAftersaleInfoMenu", 1);
		orderObj.put("gmtCreate", FormatKit.getDateTime(siteCode, systemOrder.getGmtCreate()));
		String sysSubOrderStatus = systemOrderExtMapper.selectOneByQuery(new QueryWrapper().eq("orderId", orderId)).getSysSubOrderStatus();
		orderObj.put("sysSubOrderStatus", sysSubOrderStatus);
	}

	@Override
	public List<Map<String, Object>> getOrderItemList(String siteCode, String orderId) {
		SiteBaseSitecode baseSite = baseSiteService.getBaseSite(siteCode);
		String languageCode = baseSite.getLanguageCode();
		return getOrderItemList(siteCode,languageCode,null,orderId,null,null,0);
	}

	/**
	 * @return
	 * 按钮  1隐藏  0显示
	 * hideAftersaleInfoMenu	售后详情
	 * hideAftersaleMenu		售后服务
	 *
	 */
	public List<Map<String, Object>> getOrderItemList(String siteCode, String languageCode, String id, String orderId, Integer itemId, Integer itemSkuId, int isAfterSale) {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> orderMap = getOrderMap(orderId);

		int isSync = getSyncStatus(orderMap);
		if (isAfterSale == 0) {
			list = getOrderItems(orderId, isSync, languageCode);
		} else {
			list = getAfterSaleItems(id, orderId, itemId, itemSkuId, languageCode);
		}

		if (!list.isEmpty()) {
			processOrderItems(list, siteCode, isSync);
		} else {
			list = fetchItemsFromSite(orderId);
			if (!list.isEmpty()) {
				processOrderItems(list, siteCode, 0);
			}
		}

		return list;
	}

	private Map<String, Object> getOrderMap(String orderId) {
		SystemOrder systemOrder = systemOrderMapper.selectOneById(orderId);
		if (systemOrder == null) {
			DynamicDatasourceHolder.setDataSource("site");
			SiteOrder siteOrder = siteOrderMapper.selectOneById(orderId);
			DynamicDatasourceHolder.removeDataSource();
			return EntityToMapConverter.convertToMap(siteOrder);
		} else {
			return EntityToMapConverter.convertToMap(systemOrder);
		}
	}

	private int getSyncStatus(Map<String, Object> orderMap) {
		return MapUtils.getIntegerValue(orderMap, "payStatus") == null ? 1 : MapUtils.getIntValue(orderMap, "isSync");
	}

	private List<Map<String, Object>> getOrderItems(String orderId, int isSync, String languageCode) {
		if (isSync == 0) {
			try {
				DynamicDatasourceHolder.setDataSource("site");
				return siteOrderItemsMapper.selectNotSyncByOrderId(orderId);
			} finally {
				DynamicDatasourceHolder.removeDataSource();
			}
		} else {
			return systemOrderItemsMapper.selectIsSyncByOrderId(orderId, languageCode);
		}
	}

	private List<Map<String, Object>> getAfterSaleItems(String id, String orderId, Integer itemId, Integer itemSkuId, String languageCode) {
		return systemOrderAftersaleMapper.selectAfterSaleItems(id, orderId, itemId, itemSkuId, languageCode);
	}

	private List<Map<String, Object>> fetchItemsFromSite(String orderId) {
		try {
			DynamicDatasourceHolder.setDataSource("site");
			return siteOrderItemsMapper.selectItemsByOrderId(orderId);
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	private void processOrderItems(List<Map<String, Object>> list, String siteCode, int isSync) {
		for (Map<String, Object> record : list) {
			record.put("hideAftersaleInfoMenu", 1);
			record.put("hideAftersaleMenu", 1);

			if (isSync != 0) {
				updateItemImage(record);
			}

			updatePriceInfo(record, siteCode);
			record.remove("skuSalePrice");
			record.remove("productId");
			record.remove("attrValuePath");
		}
	}

	private void updateItemImage(Map<String, Object> record) {
		try {
			DynamicDatasourceHolder.setDataSource("site");
			Integer itemSkuId = MapUtils.getIntegerValue(record, "itemSkuId");
			SiteItemSku siteItemSku = siteItemSkuMapper.selectOneById(itemSkuId);
			record.put("image", siteItemSku.getImage());
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	private void updatePriceInfo(Map<String, Object> record, String siteCode) {
		String currencyCode = MapUtils.getStringValue(record, "currencyCode");
		BigDecimal salePrice = MapUtils.getBigDecimalValue(record, "salePrice");

		if (siteCode.equals(SiteCodeValue.TYPE_EN)) {
			if ("USD".equals(currencyCode)) {
				record.put("usdPrice", record.get("salePrice"));
			} else {
				record.put("usdPrice", record.get("skuSalePrice"));
			}
		} else {
			record.put("usdPrice", FormatKit.getUsdPrice(siteCode, currencyCode, salePrice));
		}
		record.put("salePriceTxt", FormatKit.getCurrencyPriceTxt(currencyCode, salePrice));
	}

	@Override
	public Map<String, Object> getOrderConsignee(String orderId) {
        return EntityToMapConverter.convertToMap(systemOrderConsigneeMapper.selectOneByQuery(new QueryWrapper().eq("orderId", orderId)));
	}

	@Override
	public Map<String, Object> getMyOrderCount(String siteCode, String memberId, String mobile) {
		Map<String, Object> record = new HashMap<>();
		if (StringUtils.isBlank(mobile)) {
			//被拆的订单和合成的订单不算
			record = systemOrderMapper.selectOrderCount(siteCode, memberId);

			// 查询订单总数和未支付订单数
			try {
				DynamicDatasourceHolder.setDataSource("site");
				long count = siteOrderMapper.selectCountByQuery(new QueryWrapper()
						.eq("orderStatus", "SUBMITED")
						.ne("paymentType", 1)
						.eq("memberId", memberId)
						.eq("siteCode", siteCode));
				record.put("unpaid", Integer.valueOf(String.valueOf(count)));
			} finally {
				DynamicDatasourceHolder.removeDataSource();
			}
			// 查询退货订单数
			long returnsNum = systemOrderAftersaleMapper.selectCountByQuery(new QueryWrapper()
					.eq("memberId", memberId)
					.eq("siteCode", siteCode)
					.ne("status", -1)
					.lt("status", 4));
			record.put("returns", Integer.valueOf(String.valueOf(returnsNum)));

		} else {

			//处理中
			List<String> processList = systemOrderMapper.selectProcessOrderIds(siteCode, memberId, mobile);
			record.put("processing",processList == null ? 0 : processList.size());

			//运输中
			List<String> shippedList = systemOrderMapper.selectShippedOrderIds(siteCode, memberId, mobile);
			record.put("shipped", shippedList == null ? 0 : shippedList.size());
			record.put("unpaid", 0);
			record.put("returns", 0);
		}
		return record;
	}

	/**
	 * redis 同步订单数据
	 * @param orderId
	 */
	@Override
	public void syncOrder(String orderId) {
		SiteOrder order = getOrder(orderId);
		if (order == null) {
			return;
		}

		SiteOrderConsignee consignee = getConsignee(orderId);
		List<SiteOrderItems> items = getItems(orderId);

		try {
			Db.tx(() -> {
				syncOrderInfo(order);
				syncConsigneeInfo(consignee, order);
				syncItemsInfo(items);
				syncOrderExt(orderId, order);
				saveTrackIfNeeded(orderId, order);
				markOrderAsSynced(orderId);
				return true;
			});

			queueWhatsAppMessage(orderId, order);

		} catch (Exception e) {
			log.error("订单:{}同步出现异常:", orderId, e);
		}
	}

	private void syncOrderInfo(SiteOrder order) {
		SystemOrder tbSystemOrder = systemOrderMapper.selectOneById(order.getId());
		if (tbSystemOrder == null) {
			SystemOrder systemOrder = new SystemOrder();
			BeanUtils.copyProperties(order, systemOrder);
			systemOrder.setOrderStatus(determineOrderStatus(order));
			systemOrderMapper.insertSelective(systemOrder);
		}
	}

	private String determineOrderStatus(SiteOrder order) {
		if (!order.getPaymentType().equals(PaymentType.COD)) {
			return order.getPayStatus().equals(1) ? OrderStatus.ORDER_STATUS_STOCKING : OrderStatus.ORDER_STATUS_UNPAID;
		}
		return OrderStatus.ORDER_STATUS_STOCKING;
	}

	private void syncConsigneeInfo(SiteOrderConsignee consignee, SiteOrder order) {
		SystemOrderConsignee tbConsignee = systemOrderConsigneeMapper.selectOneByQuery(new QueryWrapper().eq("orderId", order.getId()));
		if (tbConsignee == null) {
			cleanConsigneeInfo(consignee, order);
			SystemOrderConsignee systemOrderConsignee = new SystemOrderConsignee();
			BeanUtils.copyProperties(consignee, systemOrderConsignee);
			systemOrderConsigneeMapper.insertSelective(systemOrderConsignee);
		}
	}

	private void cleanConsigneeInfo(SiteOrderConsignee consignee, SiteOrder order) {
		String countryCode = consignee.getCountryCode();
		if ("TH".equals(countryCode)) {
			handleThaiConsignee(consignee);
		} else if ("MY".equals(countryCode)) {
			handleMalaysianConsignee(consignee);
		} else if ("GR".equals(countryCode)) {
			handleGreekConsignee(consignee);
		} else if ("LA".equals(countryCode)) {
			handleLaotianConsignee(consignee);
		} else if ("ID".equals(countryCode)) {
			handleIndonesianConsignee(consignee);
		} else if ("PT".equals(countryCode)) {
			handlePortugueseConsignee(consignee);
		} else if ("SG".equals(countryCode)) {
			handleSingaporeanConsignee(consignee);
		} else if ("PH".equals(countryCode)) {
			handlePhilippineConsignee(consignee);
		}
	}

	private void handleThaiConsignee(SiteOrderConsignee consignee) {
		String zipcode = consignee.getZipcode();
		List<String> provinceList = getTHProvinceList(zipcode);
		if (StringUtils.isBlank(consignee.getProvince())) {
			if (provinceList.size() == 1) {
				consignee.setProvince(provinceList.get(0));
				if (StringUtils.isBlank(consignee.getCity())) {
					List<String> cityList = getTHCityList(zipcode, provinceList.get(0));
					consignee.setCity(cityList != null && cityList.size() == 1 ? cityList.get(0) : "");
				}
			} else {
				consignee.setProvince("");
			}
		}
		consignee.setProvince(StringUtils.defaultString(consignee.getProvince()).trim());
		consignee.setCity(StringUtils.defaultString(consignee.getCity()).trim());
		consignee.setDistrict("");
	}

	private void handleMalaysianConsignee(SiteOrderConsignee consignee) {
		String mobile = consignee.getMobile();
		if (StringUtils.isNotBlank(mobile)) {
			mobile = mobile.replace("+6", "").replace("+", "").replace("-", "").replace("_", "").replace("—", "").replace(" ", "");
			if (mobile.startsWith("6")) {
				mobile = mobile.substring(1);
			}
			consignee.setMobile(mobile);
		}
	}

	private void handleGreekConsignee(SiteOrderConsignee consignee) {
		String mobile = consignee.getMobile();
		if (StringUtils.isNotBlank(mobile)) {
			mobile = mobile.replace("+30", "").replace("0030-", "").replace("30-", "").replace("0030—", "").replace("30—", "")
					.replace("(30)", "").replace("（30）", "").replace("+", "").replace("-", "").replace("_", "").replace("—", "")
					.replace(" ", "");
			if (mobile.startsWith("0030")) {
				mobile = mobile.substring(4);
			}
			consignee.setMobile(mobile);
		}

		if (StringUtils.isNotBlank(consignee.getProvince())) {
			consignee.setAddress1(consignee.getAddress1() + "," + consignee.getProvince());
		}

		String zipcode = consignee.getZipcode();
		List<String> provinceList = getGRProvinceList(zipcode);
		if (provinceList.size() == 1) {
			consignee.setProvince(provinceList.get(0));
		} else {
			if (ArrayUtil.contains(provinceList.toArray(), consignee.getProvince().trim())) {
				consignee.setProvince(consignee.getProvince().trim());
			} else {
				consignee.setProvince("");
			}
		}

		consignee.setCity("");
		if (provinceList.size() == 1) {
			List<String> cityList = getGRCityList(zipcode, provinceList.get(0));
			if (cityList != null && cityList.size() == 1) {
				consignee.setCity(cityList.get(0));
			}
		} else {
			if (ArrayUtil.contains(provinceList.toArray(), consignee.getProvince().trim())) {
				List<String> cityList = getGRCityList(zipcode, consignee.getProvince().trim());
				if (cityList != null && cityList.size() == 1) {
					consignee.setCity(cityList.get(0));
				}
			}
		}

		consignee.setProvince(StringUtils.defaultString(consignee.getProvince()).trim());
		consignee.setCity(StringUtils.defaultString(consignee.getCity()).trim());
	}

	private void handleLaotianConsignee(SiteOrderConsignee consignee) {
		String mobile = consignee.getMobile();
		if (StringUtils.isNotBlank(mobile)) {
			mobile = mobile.replace("+", "").replace("-", "").replace("_", "").replace("—", "").replace("(", "").replace(")", "")
					.replace("（", "").replace("）", "").replace(" ", "");
			if (mobile.startsWith("00856")) {
				mobile = mobile.substring(5);
			}
			if (mobile.startsWith("856")) {
				mobile = mobile.substring(3);
			}
			consignee.setMobile(mobile);
		}
	}

	private void handleIndonesianConsignee(SiteOrderConsignee consignee) {
		String mobile = consignee.getMobile();
		if (StringUtils.isNotBlank(mobile)) {
			mobile = mobile.replace("+", "").replace("-", "").replace("_", "").replace("—", "").replace("(", "").replace(")", "")
					.replace("（", "").replace("）", "").replace(" ", "");
			if (mobile.startsWith("00062")) {
				mobile = mobile.substring(5);
			}
			if (mobile.startsWith("0062")) {
				mobile = mobile.substring(4);
			}
			if (mobile.startsWith("062")) {
				mobile = mobile.substring(3);
			}
			if (mobile.startsWith("62")) {
				mobile = mobile.substring(2);
			}
			consignee.setMobile(mobile);
		}
	}

	private void handlePortugueseConsignee(SiteOrderConsignee consignee) {
		String mobile = consignee.getMobile();
		if (StringUtils.isNotBlank(mobile)) {
			mobile = mobile.replace("+351", "").replace("+00351", "").replace("+", "")
					.replace("-", "").replace("_", "").replace("—", "").replace("+", "").replace(" ", "");
			if (mobile.startsWith("011351")) {
				mobile = mobile.substring(6);
			}
			if (mobile.startsWith("00351")) {
				mobile = mobile.substring(5);
			}
			if (mobile.startsWith("0351")) {
				mobile = mobile.substring(4);
			}
			if (mobile.startsWith("351")) {
				mobile = mobile.substring(3);
			}
			consignee.setMobile(mobile);
		}
	}

	private void handleSingaporeanConsignee(SiteOrderConsignee consignee) {
		String mobile = consignee.getMobile();
		if (StringUtils.isNotBlank(mobile)) {
			mobile = mobile.replace("+", "").replace("-", "").replace("_", "").replace("—", "").replace("(", "").replace(")", "")
					.replace("（", "").replace("）", "").replace(" ", "");
			if (mobile.startsWith("0065")) {
				mobile = mobile.substring(4);
			}
			if (mobile.startsWith("065")) {
				mobile = mobile.substring(3);
			}
			if (mobile.startsWith("65")) {
				mobile = mobile.substring(2);
			}
			consignee.setMobile(mobile);
		}
	}

	private void handlePhilippineConsignee(SiteOrderConsignee consignee) {
		String mobile = consignee.getMobile();
		if (StringUtils.isNotBlank(mobile)) {
			mobile = mobile.replace("+63", "").replace("+", "").replace("-", "").replace("_", "").replace("—", "").replace("(", "")
					.replace(")", "").replace("（", "").replace("）", "").replace(" ", "");
			if (mobile.startsWith("0063")) {
				mobile = mobile.substring(4);
			}
			if (mobile.startsWith("0063")) {
				mobile = mobile.substring(4);
			}
			if (mobile.startsWith("63")) {
				mobile = mobile.substring(2);
			}
			consignee.setMobile(mobile);
		}
	}

	private void syncItemsInfo(List<SiteOrderItems> items) {
		for (SiteOrderItems item : items) {
			SystemOrderItems tbItem = systemOrderItemsMapper.selectOneById(item.getId());
			if (tbItem == null) {
				SystemOrderItems systemOrderItems = new SystemOrderItems();
				BeanUtils.copyProperties(item, systemOrderItems);
				SiteItemSku siteItemSku = getItemSku(item.getItemSkuId());
				systemOrderItems.setProductId(siteItemSku.getProductId());
				systemOrderItems.setProductSkuId(siteItemSku.getProductSkuId());
				systemOrderItems.setSysStatus("UNPURCHASE");
				systemOrderItemsMapper.insertSelective(systemOrderItems);
			}
		}
	}

	private void syncOrderExt(String orderId, SiteOrder order) {
		SystemOrderExt tbSystemOrderExt = getOrderExt(orderId);
		if (tbSystemOrderExt == null) {
			tbSystemOrderExt = new SystemOrderExt();
			tbSystemOrderExt.setOrderId(orderId);
			tbSystemOrderExt.setSysOrderStatus(OrderStatus.ORDER_STATUS_SUBMITED);
			if ("TW".equals(order.getCountryCode()) || "TH".equals(order.getCountryCode())) {
				tbSystemOrderExt.setCustomerConfirmFlag(101);
			}
			tbSystemOrderExt.setGmtCreated(order.getGmtCreate());
			systemOrderExtMapper.insertSelective(tbSystemOrderExt);
		}
	}

	private void saveTrackIfNeeded(String orderId, SiteOrder order) {
		SystemOrder tbSystemOrder = systemOrderMapper.selectOneById(orderId);
		if (tbSystemOrder == null) {
			saveTrack(orderId, order);
		}
	}

	private void markOrderAsSynced(String orderId) {
		DynamicDatasourceHolder.setDataSource("site");
		SiteOrder siteOrder = new SiteOrder();
		siteOrder.setId(orderId);
		siteOrder.setIsSync(1);
		siteOrderMapper.update(siteOrder);
	}

	private void queueWhatsAppMessage(String orderId, SiteOrder order) {
		try {
			String countryCode = order.getCountryCode();
			Jedis resource = jedisPool.getResource();
			if ("TW".equals(countryCode) || "TH".equals(countryCode)) {
				resource.lpush(WHATSAPP_ORDER_CONFIRM, orderId);
			} else if (!"VN".equals(countryCode) && !"PH".equals(countryCode)) {
				SystemMemberWhatsappBusiness systemMemberWhatsappBusiness = systemMemberWhatsappBusinessMapper.selectOneByQuery(new QueryWrapper().eq("status", 1).eq("siteCode", order.getSiteCode()));
				if (systemMemberWhatsappBusiness != null) {
					resource.lpush(WHATSAPP_ORDER_CONFIRM, orderId);
				} else {
					log.info("订单:{} 加入队列失败 {} 原因:账号不存在或者没有启用", orderId, WHATSAPP_ORDER_CONFIRM);
				}
			}
			log.info("订单:{} 加入队列 {}", orderId, WHATSAPP_ORDER_CONFIRM);
		} catch (Exception e) {
			log.error("订单:{} 加入队列出现异常:", orderId, e);
		}
	}


	/**
	 * 初始化到备货中或者待支付
	 * 只添加小状态
	 * @param orderId
	 * @param siteOrder
	 */
	private void saveTrack(String orderId, SiteOrder siteOrder) {
		// 基础状态
		insertTrackingRecord(orderId, OrderTrackStatus.ORDER_STATUS_ORDER_REMINDER);

		if (siteOrder.getPayStatus().equals(1)) {
			// 已支付
			insertTrackingRecord(orderId, OrderTrackStatus.ORDER_STATUS_PAID_REMINDER);
			// 备货中
			insertTrackingRecord(orderId, OrderTrackStatus.ORDER_STATUS_STOCK_REMINDER);
		} else {
			// 根据支付类型确定状态
			if (!siteOrder.getPaymentType().equals(PaymentType.COD)) {
				// 待支付
				insertTrackingRecord(orderId, OrderTrackStatus.ORDER_STATUS_UNPAID_REMINDER);
			} else {
				// 备货中
				insertTrackingRecord(orderId, OrderTrackStatus.ORDER_STATUS_STOCK_REMINDER);
			}
		}
	}

	private void insertTrackingRecord(String orderId, String statusCode) {
		SystemOrderTrackingRecord trackingRecord = new SystemOrderTrackingRecord();
		trackingRecord.setOrderId(orderId);
		trackingRecord.setStatusCode(statusCode);
		trackingRecord.setGmtCreate(LocalDateTime.now());
		systemOrderTrackingRecordMapper.insertSelective(trackingRecord);
	}


	/**
	 * site_order
	 * @param orderId
	 * @return
	 */
	public SiteOrder getOrder(String orderId){
		SiteOrder siteOrder = null;
		try {
			DynamicDatasourceHolder.setDataSource("site");
			siteOrder = siteOrderMapper.selectOneByQuery(new QueryWrapper().eq("isSync", 0).eq("id", orderId));
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
		return siteOrder;
	}

	/**
	 * tb_order_blacklist
	 * @return
	 */
	public List<SystemOrderBlacklist> getBlacklist() {
		return systemOrderBlacklistMapper.selectListByQuery(new QueryWrapper().eq("status", 1).orderBy("gmtCreate"));
	}
	/**
	 * site_order_items
	 * @param orderId
	 * @return
	 */
	public List<SiteOrderItems> getItems(String orderId){
		try {
			DynamicDatasourceHolder.setDataSource("site");
			return siteOrderItemsMapper.selectListByQuery(new QueryWrapper().eq("orderId", orderId));
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	/**
	 * site_item_sku
	 * @param skuId
	 * @return
	 */
	public SiteItemSku getItemSku(int skuId){
		SiteItemSku siteItemSku;
		try {
			DynamicDatasourceHolder.setDataSource("site");
			siteItemSku = siteItemSkuMapper.selectOneById(skuId);
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
		return siteItemSku;
	}

	/**
	 * site_order_consignee
	 * @param orderId
	 * @return
	 */
	public SiteOrderConsignee getConsignee(String orderId){
		SiteOrderConsignee siteOrderConsignee = null;
		try {
			DynamicDatasourceHolder.setDataSource("site");
			siteOrderConsignee = siteOrderConsigneeMapper.selectOneByQuery(new QueryWrapper().eq("orderId", orderId));
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
		return siteOrderConsignee;
	}

	/**
	 * tb_order_ext
	 * @param orderId
	 * @return
	 */
	public SystemOrderExt getOrderExt(String orderId){
        return systemOrderExtMapper.selectOneByQuery(new QueryWrapper().eq("orderId", orderId));
	}

	/**
	 * tb_order
	 * @param orderId
	 * @return
	 */
	@Override
	public SystemOrder getMemberOrder(String orderId){
		 return systemOrderMapper.selectOneById(orderId);
	}

	@Override
	public Ret getOrderTrackInfos(String orderId, String language, String siteCode) {
		Map<String, Object> order = fetchOrderData(orderId);
		if (order == null) {
			return Ret.fail();
		}

		String mergeId = getMergeId(orderId);
		List<Map<String, Object>> list = fetchTrackingRecords(orderId, language, mergeId);

		Map<String, Object> consignee = fetchConsignee(orderId, siteCode, language);
		addExpressNo(order, mergeId, siteCode);

		Map<String, Object> orderTrack = new HashMap<>();
		orderTrack.put("orderTrackInfos", list);
		orderTrack.put("consigneeInfo", consignee);
		orderTrack.put("order", order);

		return Ret.ok("result", orderTrack);
	}

	private Map<String, Object> fetchOrderData(String orderId) {
		Map<String, Object> order = null;
		try {
			DynamicDatasourceHolder.setDataSource("site");
			order = siteOrderMapper.selectOrderInfoById(orderId);
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
		if (order == null) {
			order = systemOrderMapper.selectOrderInfoById(orderId);
		}
		return order;
	}

	private String getMergeId(String orderId) {
		SystemOrder tbSystemOrder = systemOrderMapper.selectOneById(orderId);
		if (tbSystemOrder == null || StringUtils.isBlank(tbSystemOrder.getMergeId())) {
			return orderId;
		}
		return tbSystemOrder.getMergeId();
	}

	private List<Map<String, Object>> fetchTrackingRecords(String orderId, String language, String mergeId) {
		List<Map<String, Object>> list = systemOrderTrackingRecordMapper.selectOrderTrackingRecordList(orderId, language);

		if (list.isEmpty()) {
			list = initializeTrackingRecords(orderId, language);
		} else {
			processTrackingRecords(list, mergeId, language);
		}

		return list;
	}

	private List<Map<String, Object>> initializeTrackingRecords(String orderId, String language) {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> order;
		try {
			DynamicDatasourceHolder.setDataSource("site");
			order = siteOrderMapper.selectOrderInfoById(orderId);
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}

		if (MapUtils.getIntValue(order, "paymentType") == PaymentType.COD) {
			if (MapUtils.getIntValue(order, "payStatus") == 1) {
				list.add(createTrackingRecord("PAID", language, order));
			}

			list.add(createTrackingRecord("SUBMITED", language, order));
		} else {
			if (MapUtils.getIntValue(order, "payStatus") == 1) {
				list.add(createTrackingRecord("PAID", language, order));
			} else {
				list.add(createTrackingRecord("UNPAID", language, order));
			}

			list.add(createTrackingRecord("SUBMITED", language, order));
		}

		return list;
	}

	private Map<String, Object> createTrackingRecord(String status, String language, Map<String, Object> order) {
		Map<String, Object> record = systemOrderTrackingStatusMapper.selectTrackingWithStatus(language, status);
		if (record != null) {
			record.put("selected", "PAID".equals(status) || "UNPAID".equals(status));
			record.put("day", order.get("day"));
			record.put("hour", order.get("hour"));
			record.put("orderTrackInfos", new ArrayList<>());
			record.remove("id");
		}
		return record;
	}

	private void processTrackingRecords(List<Map<String, Object>> list, String mergeId, String language) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("selected", i == 0);
			List<Map<String, Object>> sublist = systemOrderTrackingRecordMapper.selectListWithParentId(mergeId, language, MapUtils.getIntValue(list.get(i), "id"));
			list.get(i).put("orderTrackInfos", sublist);
			list.get(i).remove("id");
		}
	}

	private Map<String, Object> fetchConsignee(String orderId, String siteCode, String language) {
		Map<String, Object> consignee = null;
		SystemOrderConsignee systemOrderConsignee = systemOrderConsigneeMapper.selectOneByQuery(new QueryWrapper().eq("orderId", orderId));
		if (systemOrderConsignee == null) {
			try {
				DynamicDatasourceHolder.setDataSource("site");
				SiteOrderConsignee siteOrderConsignee = siteOrderConsigneeMapper.selectOneByQuery(new QueryWrapper().eq("orderId", orderId));
				if (siteOrderConsignee != null) {
					consignee = EntityToMapConverter.convertToMap(siteOrderConsignee);
				}
			} finally {
				DynamicDatasourceHolder.removeDataSource();
			}
		} else {
			consignee = EntityToMapConverter.convertToMap(systemOrderConsignee);
		}

		if (consignee != null) {
			Map<String, Object> status = systemOrderTrackingStatusMapper.selectTrackingStatus("ADDRESS", language);
			if (status != null) {
				consignee.put("statusIcon", status.get("statusIcon"));
				consignee.put("statusName", status.get("statusName"));
			}

			Map<String, Object> map = new HashMap<>();
			map.put("country", consignee.get("country"));
			map.put("province", consignee.get("province"));
			map.put("city", consignee.get("city"));
			map.put("district", consignee.get("district"));
			map.put("address1", consignee.get("address1"));
			map.put("address2", consignee.get("address2"));
			map.put("zipcode", consignee.get("zipcode"));

			String addressTxt = templateService.renderDynamicTemplate(baseSiteService.getBaseSite(siteCode).getAddressFormat(), map);
			addressTxt = addressTxt.replace(";", ", ") + "  (" + consignee.get("mobile") + ")";
			consignee.put("addressTxt", addressTxt);
			removeUnusedConsigneeFields(consignee);
		}

		return consignee;
	}

	private void removeUnusedConsigneeFields(Map<String, Object> consignee) {
		consignee.remove("province");
		consignee.remove("city");
		consignee.remove("district");
		consignee.remove("address1");
		consignee.remove("address2");
		consignee.remove("zipcode");
		consignee.remove("fullName");
		consignee.remove("mobile");
	}

	private void addExpressNo(Map<String, Object> order, String mergeId, String siteCode) {
		SystemOrderExt systemOrderExt = systemOrderExtMapper.selectOneByQuery(new QueryWrapper().eq("orderId", mergeId));
		if (systemOrderExt == null || StringUtils.isBlank(systemOrderExt.getExpressNo())) {
			order.put("expressNo", "");
		} else {
			order.put("expressNo", systemOrderExt.getExpressNo());
		}
		order.put("gmtCreate", FormatKit.getDateTime(siteCode, (LocalDateTime) order.get("gmtCreate")));
	}


	public SystemOrderTrackingRecord getOrderTrackRecord(String orderId, String statusCode) {
		return systemOrderTrackingRecordMapper.selectOneByQuery(new QueryWrapper().eq("orderId", orderId).eq("statusCode", statusCode));
	}

	@Override
	public void updateOrder(String orderId, Integer reviewStatus, Integer payStatus, String orderStatus, Integer paymentId, Integer status) {
		SystemOrder systemOrder = getMemberOrder(orderId);

		if (systemOrder == null) {
			return; // 如果系统订单为空，直接返回
		}

		LocalDateTime now = LocalDateTime.now();

		// 更新评论状态
		if (reviewStatus != null) {
			systemOrder.setReviewStatus(reviewStatus);
			systemOrder.setGmtUpdate(now);
		}

		// 更新支付信息
		if (paymentId != null) {
			updateByPaymentId(orderId, paymentId, systemOrder);

			if (payStatus != null) {
				updateByPayStatus(orderId, payStatus, systemOrder);
			}

			// 同步支付状态到其他数据源
			syncPaymentInfo(orderId, paymentId, payStatus, now);
		}

		// 更新状态
		if (status != null) {
			systemOrder.setStatus(status);
			systemOrder.setGmtUpdate(now);
		}

		// 统一更新 systemOrder
		systemOrderMapper.update(systemOrder);
	}

	private void syncPaymentInfo(String orderId, Integer paymentId, Integer payStatus, LocalDateTime now) {
		try {
			DynamicDatasourceHolder.setDataSource("site");
			SiteOrder siteOrder = siteOrderMapper.selectOneById(orderId);

			if (siteOrder == null) {
				handleOrder(orderId);
				return;
			}

			if (siteOrder.getSiteCode().equals(SiteCodeValue.TYPE_EN)) {
				handleOrder(orderId);
			} else {
				siteOrder.setPaymentType(paymentId);
				siteOrder.setGmtUpdate(now);
				siteOrderMapper.update(siteOrder);
			}

			if (siteOrder.getIsSync().equals(0) && siteOrder.getPayStatus().equals(-2)) {
				if (siteOrder.getTotalWalletPrice().compareTo(BigDecimal.ZERO) > 0 && payStatus == 1) {
					memberService.updateWalletRecord(orderId, "Completed");
				}
			}
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	private void updateByPayStatus(String orderId, Integer payStatus, SystemOrder systemOrder) {
		LocalDateTime now = LocalDateTime.now();

		Db.tx(() -> {
			// 更新支付状态
			systemOrder.setPayStatus(payStatus);

			// 仅在支付成功时执行额外操作
			if (payStatus.equals(1)) {
				systemOrder.setOrderStatus(OrderStatus.ORDER_STATUS_PAID);
				systemOrder.setGmtUpdate(now);

				// 插入订单跟踪记录
				handleOrderTrackingRecord(orderId, now);

				// 更新钱包记录
				if (systemOrder.getTotalWalletPrice().compareTo(BigDecimal.ZERO) > 0) {
					memberService.updateWalletRecord(orderId, "Completed");
				}

				// 更新扩展订单信息
				updateOrderExt(orderId);
			}

			// 更新订单
			systemOrderMapper.update(systemOrder);

			return true;
		});
	}

	private void handleOrderTrackingRecord(String orderId, LocalDateTime now) {
		SystemOrderTrackingRecord existingRecord = getOrderTrackRecord(orderId, OrderTrackStatus.ORDER_STATUS_PAID_REMINDER);

		if (existingRecord == null) {
			insertOrderTrackingRecord(orderId, OrderTrackStatus.ORDER_STATUS_PAID_REMINDER, now);
			insertOrderTrackingRecord(orderId, OrderTrackStatus.ORDER_STATUS_STOCK_REMINDER, now);
		}
	}

	private void insertOrderTrackingRecord(String orderId, String statusCode, LocalDateTime now) {
		SystemOrderTrackingRecord record = new SystemOrderTrackingRecord();
		record.setOrderId(orderId);
		record.setStatusCode(statusCode);
		record.setGmtCreate(now);
		systemOrderTrackingRecordMapper.insertSelective(record);
	}

	private void updateOrderExt(String orderId) {
		SystemOrderExt systemOrderExt = getOrderExt(orderId);
		systemOrderExt.setConfirmFlag(1);
		systemOrderExt.setConfirmType("");
		systemOrderExt.setSystemCheckFlag(0);
		systemOrderExt.setSystemRemark("");
		systemOrderExtMapper.update(systemOrderExt);
	}

	private void updateByPaymentId(String orderId, Integer paymentId, SystemOrder systemOrder) {
		LocalDateTime now = LocalDateTime.now();

		if (paymentId.equals(PaymentType.COD)) {
			handleCODPayment(orderId, systemOrder, now);
		} else {
			handleOtherPaymentTypes(orderId, paymentId, systemOrder, now);
		}
	}

	private void handleCODPayment(String orderId, SystemOrder systemOrder, LocalDateTime now) {
		if (systemOrder.getSiteCode().equals(SiteCodeValue.TYPE_EN)) {
			handleOrder(orderId);
		} else {
			updateOrderAndSiteOrder(orderId, systemOrder, PaymentType.COD, OrderStatus.ORDER_STATUS_STOCKING, now);
		}

		deleteUnpaidRecords(orderId);
		updateTrackingRecord(orderId, OrderTrackStatus.ORDER_STATUS_UNPAID_REMINDER, OrderTrackStatus.ORDER_STATUS_STOCK_REMINDER, now);
	}

	private void handleOtherPaymentTypes(String orderId, Integer paymentId, SystemOrder systemOrder, LocalDateTime now) {
		systemOrder.setPaymentType(paymentId);
		systemOrder.setGmtUpdate(now);
		systemOrderMapper.update(systemOrder);

		updateSiteOrder(orderId, paymentId, now);

		if (paymentId.equals(PaymentType.BRAINTREE_CREDIT_CARD) || paymentId.equals(PaymentType.BRAINTREE_PAYPAL)) {
			deleteUnpaidRecords(orderId);
		}
	}

	private void updateOrderAndSiteOrder(String orderId, SystemOrder systemOrder, Integer paymentType, String orderStatus, LocalDateTime now) {
		systemOrder.setPaymentType(paymentType);
		systemOrder.setOrderStatus(orderStatus);
		systemOrder.setGmtUpdate(now);
		systemOrderMapper.update(systemOrder);

		try {
			DynamicDatasourceHolder.setDataSource("site");
			SiteOrder siteOrder = siteOrderMapper.selectOneById(orderId);
			if (siteOrder != null) {
				siteOrder.setPaymentType(paymentType);
				siteOrder.setGmtUpdate(now);
				siteOrderMapper.update(siteOrder);
			}
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	private void updateSiteOrder(String orderId, Integer paymentId, LocalDateTime now) {
		try {
			DynamicDatasourceHolder.setDataSource("site");
			SiteOrder siteOrder = siteOrderMapper.selectOneById(orderId);
			if (siteOrder != null) {
				siteOrder.setPaymentType(paymentId);
				siteOrder.setGmtUpdate(now);
				siteOrderMapper.update(siteOrder);
			}
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	private void deleteUnpaidRecords(String orderId) {
		try {
			DynamicDatasourceHolder.setDataSource("site");
			SiteOrderBill siteOrderBill = siteOrderBillMapper.selectOneByQuery(
					new QueryWrapper().eq("status", 0).eq("orderId", orderId));
			if (siteOrderBill != null) {
				siteOrderBillMapper.delete(siteOrderBill);
			}
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	private void updateTrackingRecord(String orderId, String oldStatus, String newStatus, LocalDateTime now) {
		SystemOrderTrackingRecord existingRecord = getOrderTrackRecord(orderId, oldStatus);

		if (existingRecord != null) {
			systemOrderTrackingRecordMapper.delete(existingRecord);
		}

		SystemOrderTrackingRecord newRecord = new SystemOrderTrackingRecord();
		newRecord.setOrderId(orderId);
		newRecord.setStatusCode(newStatus);
		newRecord.setGmtCreate(now);
		systemOrderTrackingRecordMapper.insertSelective(newRecord);
	}


	private void handleOrder(String orderId) {
		LocalDateTime now = LocalDateTime.now();
		SiteOrder order;
		String siteCode;
		String defaultCurrency;

		try {
			DynamicDatasourceHolder.setDataSource("site");
			order = siteOrderMapper.selectOneById(orderId);
			siteCode = order.getSiteCode();
			defaultCurrency = siteOrderConsigneeMapper.selectDefaultCurrency(orderId);
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}

		String currencyCode = FormatKit.getSiteBySiteCode(siteCode).getCurrencyCode();

		if (!defaultCurrency.equals(currencyCode)) {
			updateOrderForDifferentCurrency(order, orderId, siteCode, defaultCurrency, currencyCode, now);
		} else {
			updateOrderForSameCurrency(order, now);
		}
	}

	private void updateOrderForDifferentCurrency(SiteOrder order, String orderId, String siteCode, String defaultCurrency, String currencyCode, LocalDateTime now) {
		BigDecimal totalSalePrice = BigDecimal.ZERO;
		BigDecimal totalShippingPrice = BigDecimal.ZERO;
		BigDecimal totalPromoPrice = BigDecimal.ZERO;
		BigDecimal totalCouponPrice = BigDecimal.ZERO;
		BigDecimal totalPointPrice = BigDecimal.ZERO;
		BigDecimal totalWalletPrice = BigDecimal.ZERO;
		BigDecimal totalPayPrice = BigDecimal.ZERO;

		List<SiteOrderItems> siteOrderItems = getItems(orderId);

		for (SiteOrderItems orderItem : siteOrderItems) {
			processOrderItem(orderItem, siteCode, defaultCurrency, now, order.getIsSync());
			totalSalePrice = totalSalePrice.add(orderItem.getSubTotalSalePrice());
			totalShippingPrice = totalShippingPrice.add(orderItem.getSubTotalShippingPrice());
			totalPromoPrice = totalPromoPrice.add(orderItem.getSubTotalPromoPrice());
			totalCouponPrice = totalCouponPrice.add(orderItem.getSubTotalCouponPrice());
			totalPointPrice = totalPointPrice.add(orderItem.getSubTotalPointPrice());
			totalWalletPrice = totalWalletPrice.add(orderItem.getSubTotalWalletPrice());
			totalPayPrice = totalPayPrice.add(orderItem.getSubTotalPayPrice());
		}

		updateOrderTotals(order, defaultCurrency, siteCode, currencyCode, totalSalePrice, totalShippingPrice, totalPromoPrice, totalCouponPrice, totalPointPrice, totalWalletPrice, totalPayPrice, now);

		try {
			DynamicDatasourceHolder.setDataSource("site");
			siteOrderMapper.update(order);
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	private void processOrderItem(SiteOrderItems orderItem, String siteCode, String defaultCurrency, LocalDateTime now, Integer isSync) {
		BigDecimal subTotalSalePrice = BigDecimal.ZERO;
		BigDecimal subTotalPayPrice = BigDecimal.ZERO;
		Integer buyNum = orderItem.getBuyNum();

		orderItem.setCurrencyCode(defaultCurrency);
		orderItem.setSalePrice(FormatKit.getPrice(siteCode, defaultCurrency, orderItem.getSalePrice()));
		orderItem.setSubTotalShippingPrice(FormatKit.getPrice(siteCode, defaultCurrency, orderItem.getSubTotalShippingPrice()));
		orderItem.setSubTotalPromoPrice(FormatKit.getPrice(siteCode, defaultCurrency, orderItem.getSubTotalPromoPrice()));
		orderItem.setSubTotalCouponPrice(FormatKit.getPrice(siteCode, defaultCurrency, orderItem.getSubTotalCouponPrice()));
		orderItem.setSubTotalPointPrice(FormatKit.getPrice(siteCode, defaultCurrency, orderItem.getSubTotalPointPrice()));
		orderItem.setSubTotalWalletPrice(FormatKit.getPrice(siteCode, defaultCurrency, orderItem.getSubTotalWalletPrice()));

		subTotalSalePrice = orderItem.getSalePrice().multiply(new BigDecimal(buyNum));
		subTotalPayPrice = subTotalSalePrice.subtract(orderItem.getSubTotalPromoPrice())
				.subtract(orderItem.getSubTotalCouponPrice())
				.subtract(orderItem.getSubTotalPointPrice())
				.subtract(orderItem.getSubTotalWalletPrice());

		orderItem.setSubTotalSalePrice(subTotalSalePrice);
		orderItem.setSubTotalPayPrice(subTotalPayPrice);

		orderItem.setGmtUpdate(now);

		try {
			DynamicDatasourceHolder.setDataSource("site");
			siteOrderItemsMapper.update(orderItem);
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}

		if (isSync.equals(1)) {
			updateSystemOrderItems(orderItem);
		}
	}

	private void updateSystemOrderItems(SiteOrderItems orderItem) {
		SystemOrderItems tbItems = systemOrderItemsMapper.selectOneById(orderItem.getId());
		if (tbItems != null) {
			SystemOrderItems newItem = new SystemOrderItems();
			BeanUtils.copyProperties(orderItem, newItem);
			newItem.setSysStatus(tbItems.getSysStatus());
			systemOrderItemsMapper.update(newItem);
		}
	}

	private void updateOrderTotals(SiteOrder order, String defaultCurrency, String siteCode, String currencyCode, BigDecimal totalSalePrice, BigDecimal totalShippingPrice, BigDecimal totalPromoPrice, BigDecimal totalCouponPrice, BigDecimal totalPointPrice, BigDecimal totalWalletPrice, BigDecimal totalPayPrice, LocalDateTime now) {
		order.setCurrencyCode(defaultCurrency);
		order.setTotalSalePrice(totalSalePrice);
		order.setTotalShippingPrice(totalShippingPrice);
		order.setTotalPromoPrice(totalPromoPrice);
		order.setTotalCouponPrice(totalCouponPrice);
		order.setTotalPointPrice(totalPointPrice);
		order.setTotalWalletPrice(totalWalletPrice);
		order.setPaymentType(PaymentType.COD);
		order.setGmtUpdate(now);
		totalPayPrice = totalPayPrice.add(order.getTotalShippingPrice());

		if (order.getIsSync().equals(1)) {
			updateSystemOrderForSync(order, siteCode, currencyCode);
		}

		order.setTotalPayPrice(totalPayPrice);
		order.setOrderStatus("SUBMITED");
	}

	private void updateSystemOrderForSync(SiteOrder order, String siteCode, String currencyCode) {
		SystemOrder tbSystemOrder = systemOrderMapper.selectOneById(order.getId());
		if (tbSystemOrder != null) {
			order.setTotalReducePrice(FormatKit.getPrice(siteCode, currencyCode, tbSystemOrder.getTotalReducePrice()));
			order.setTotalVipPrice(FormatKit.getPrice(siteCode, currencyCode, tbSystemOrder.getTotalVipPrice()));

			BigDecimal totalPayPrice = order.getTotalPayPrice()
					.subtract(order.getTotalReducePrice())
					.subtract(order.getTotalVipPrice());

			order.setTotalPayPrice(totalPayPrice);
			order.setOrderStatus(OrderStatus.ORDER_STATUS_STOCKING);

			SystemOrder newSystemOrder = new SystemOrder();
			BeanUtils.copyProperties(order, newSystemOrder);
			systemOrderMapper.update(newSystemOrder);
		}
	}

	private void updateOrderForSameCurrency(SiteOrder order, LocalDateTime now) {
		if (order.getIsSync().equals(1)) {
			order.setPaymentType(PaymentType.COD);
			order.setOrderStatus(OrderStatus.ORDER_STATUS_STOCKING);
			order.setGmtUpdate(now);
			SystemOrder newSystemOrder = new SystemOrder();
			BeanUtils.copyProperties(order, newSystemOrder);
			systemOrderMapper.update(newSystemOrder);
		}

		try {
			DynamicDatasourceHolder.setDataSource("site");
			SiteOrder siteOrder = siteOrderMapper.selectOneById(order.getId());
			siteOrder.setPaymentType(PaymentType.COD);
			siteOrder.setGmtUpdate(now);
			siteOrderMapper.update(siteOrder);
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	@Override
	public List<Map<String, Object>> getAftersaleReason(String reasonType,String languageCode){
		return systemOrderAftersaleReasonMapper.selectWithReasonType(languageCode, reasonType);
	}

	/**
	 *
	 * @param siteCode
	 * @param languageCode
	 * @param itemId
	 * @param itemSkuId
	 * @param orderId
	 * @param memberId
	 * @return
	 *
	 * 按钮   1隐藏  0显示
	 * hideUpdateMenu		更新
	 * hideCancelMenu		取消/撤销
	 * hideExpressMenu		添加快递
	 * hideUpdateExpressMenu修改快递
	 */
	@Override
	public Map<String, Object> getAftersaleInfo(String siteCode, String languageCode, Integer itemId, Integer itemSkuId, String orderId, Integer id, String memberId) {
		Map<String, Object> record = systemOrderAftersaleMapper.selectAfterSaleInfo(siteCode, orderId, itemId, itemSkuId, memberId);

		if (record == null) {
			return null;
		}

		int status = MapUtils.getIntValue(record, "status");
		int processType = MapUtils.getIntValue(record, "processType");

		// 默认隐藏菜单
		setDefaultMenuVisibility(record);

		if (status == 0) {
			handlePendingReview(record);
		} else if (status == 2) {
			handleApproved(record, processType);
		} else if (status == -2) {
			// 审核拒绝，菜单已隐藏
		} else if (status >= 3) {
			// 其他状态，菜单已隐藏
		}

		// 处理图片证明
		record.put("imgList", processImageProof(record.get("imgProof")));

		// 格式化申请金额
		record.put("applyAmountTxt", FormatKit.getCurrencyPriceTxt((String) record.get("currencyCode"), MapUtils.getBigDecimalValue(record, "applyAmount")));

		// 获取订单项
		Map<String, Object> orderItem = systemOrderItemsMapper.selectWithOrderIdAndItemId(orderId, itemId, languageCode);
		record.put("title", orderItem.get("title"));
		record.put("skuAttrNames", orderItem.get("skuAttrNames"));

		// 获取 SKU 图片
		setSkuImage(record, itemSkuId);

		// 当前申请售后状态
		updateStatusList(record, languageCode, status, processType);

		// 提醒标题及详情
		record.put("remindTitle", getRemindTitle(languageCode, status));

		return record;
	}

	private void setDefaultMenuVisibility(Map<String, Object> record) {
		record.put("hideUpdateMenu", 1);
		record.put("hideCancelMenu", 1);
		record.put("hideExpressMenu", 1);
	}

	private void handlePendingReview(Map<String, Object> record) {
		LocalDateTime checkTime = (LocalDateTime) record.getOrDefault("gmtUpdate", record.get("gmtCreate"));
		if (checkTime != null) {
			long leftSeconds = Duration.between(LocalDateTime.now(), checkTime.plusDays(3)).getSeconds();
			record.put("leftSeconds", Math.max(0, (int) leftSeconds));
		} else {
			record.put("leftSeconds", 0);
		}
	}

	private void handleApproved(Map<String, Object> record, int processType) {
		if (processType == 0) { // 仅退款
			record.put("hideExpressMenu", 1);
			record.put("hideUpdateExpressMenu", 1);
		} else if (record.get("expressNo") == null) {
			record.put("hideUpdateExpressMenu", 1);
			record.put("hideExpressMenu", 1);
		}
	}

	private List<String> processImageProof(Object imgProof) {
		return (imgProof != null && StringUtils.isNotBlank(imgProof.toString())) ? Arrays.asList(imgProof.toString().split(",")) : Collections.emptyList();
	}

	private void setSkuImage(Map<String, Object> record, Integer itemSkuId) {
		try {
			DynamicDatasourceHolder.setDataSource("site");
			SiteItemSku siteItemSku = siteItemSkuMapper.selectOneById(itemSkuId);
			if (siteItemSku != null) {
				record.put("image", siteItemSku.getImage());
			}
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	private void updateStatusList(Map<String, Object> record, String languageCode, Integer nowStatus, Integer processType) {
		List<Map<String, Object>> statusList = systemOrderAftersaleStatusAliasMapper.selectStatusInfo(languageCode, processType, nowStatus);

		Map<Integer, Integer> statusMapping = new HashMap<>();
		statusList.forEach(statusMap -> {
			Integer statusValue = MapUtils.getIntegerValue(statusMap, "status");
			statusMapping.put(statusValue, determineStatusSelection(nowStatus, statusValue, processType));
		});

		statusList.forEach(statusMap -> statusMap.put("selected", statusMapping.getOrDefault(MapUtils.getIntegerValue(statusMap, "status"), -1)));
	}

	private Integer determineStatusSelection(Integer nowStatus, Integer statusValue, Integer processType) {
		if (nowStatus.equals(0)) {
			return statusValue.equals(0) ? 1 : (statusValue.equals(10) ? 0 : -1);
		}
		if (nowStatus.equals(2)) {
			return (statusValue.equals(0) || statusValue.equals(10)) ? 1 : (processType == 2 ? (statusValue.equals(4) ? 0 : -1) : (statusValue.equals(3) ? 0 : -1));
		}
		if (nowStatus.equals(-2)) {
			return (statusValue.equals(0) || statusValue.equals(10)) ? 1 : -1;
		}
		if (nowStatus >= 3) {
			return (statusValue <= nowStatus) ? 1 : (statusValue.equals(10) ? 1 : (statusValue.equals(nowStatus + 1) ? 0 : -1));
		}
		return -1;
	}

	private Map<String, Object> getRemindTitle(String languageCode, Integer status) {
		Map<String, Object> remindTitle = systemOrderAftersaleStatusAliasMapper.selectTitle(languageCode, status);
		if (remindTitle != null) {
			String remindTitleDes = (String) remindTitle.get("remindTitleDes");
			remindTitle.put("remindTitleDes", (remindTitleDes != null && remindTitleDes.contains("<br/>")) ? Arrays.asList(remindTitleDes.split("<br/>")) : Collections.singletonList(remindTitleDes));
		}
		return remindTitle;
	}

	@Override
	public Page<Map<String, Object>> getAftersaleList(String siteCode, String memberId, int pageSize, int pageNumber){
		Page<Map<String, Object>> page = new Page<>();
		List<Map<String, Object>> records = systemOrderAftersaleMapper.pageOrderAfterSale(siteCode, memberId, null, pageSize, (pageNumber - 1) * pageSize);
		Integer count = systemOrderAftersaleMapper.countOrderAfterSale(siteCode, memberId, null);
		page.setRecords(records);
		page.setTotalRow(count);
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		page.setTotalPage((int) Math.ceil((double) count / pageSize));
		return page;
	}

	public Page<Map<String, Object>> getAftersaleList(String siteCode, String mobile, String memberId, int pageSize, int pageNumber){
		Page<Map<String, Object>> page = new Page<>();
		List<Map<String, Object>> records = systemOrderAftersaleMapper.pageOrderAfterSale(siteCode, memberId, mobile, pageSize, (pageNumber - 1) * pageSize);
		Integer count = systemOrderAftersaleMapper.countOrderAfterSale(siteCode, memberId, mobile);
		page.setRecords(records);
		page.setTotalRow(count);
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		page.setTotalPage((int) Math.ceil((double) count / pageSize));
		return page;
	}

	@Override
	public SystemOrderAftersale getAftersaleApplyNum(String siteCode, Integer itemId, Integer itemSkuId, String orderId, String memberId){
        return systemOrderAftersaleMapper.selectOneByQuery(new QueryWrapper()
				.eq("orderId", orderId)
				.eq("itemId", itemId)
				.eq("itemSkuId", itemSkuId)
				.eq("memberId", memberId)
				.ne("status", -1));

	}

	public String getAftersaleStatus(String languageCode,Integer statusType,Integer status){
		return systemOrderAftersaleStatusAliasMapper.selectStatusName(languageCode, statusType, status);
	}

	@Override
	public Integer saveOrderAftersale(String siteCode,String languageCode,String currencyCode,Integer id,String orderId,Integer itemId,Integer itemSkuId,String memberId,String imgProof,Integer processType,Integer applyNum,BigDecimal applyAmount,String reasonCode,String applyReason,String applyDes,Integer isReceive){
		Db.tx(()->{
			int from = 1;
			SystemOrder systemOrder = systemOrderMapper.selectOneByQuery(new QueryWrapper()
					.eq("id", id)
					.eq("siteCode", siteCode)
					.eq("memberId", memberId));
			if(systemOrder == null){
				SiteOrder siteOrder = getOrder(orderId);
				from = 2;
				if (siteOrder != null) {
					systemOrder = new SystemOrder();
					BeanUtils.copyProperties(siteOrder, systemOrder);
				}
			}

			if(systemOrder != null){
				SystemOrderExt systemOrderExt = systemOrderExtMapper.selectOneByQuery(new QueryWrapper().eq("orderId", orderId));
				if(systemOrderExt != null){
					systemOrderExt.setProcessType(processType);
					systemOrderExt.setGmtUpdate(LocalDateTime.now());
					systemOrderExtMapper.update(systemOrderExt);
				}
				SystemOrderItems systemOrderItems = systemOrderItemsMapper.selectOneByQuery(new QueryWrapper().eq("orderId", orderId).eq("itemId", itemId));

				if(systemOrderItems != null){
					systemOrderItems.setProcessType(processType);
					systemOrderItems.setGmtUpdate(LocalDateTime.now());
					systemOrderItemsMapper.update(systemOrderItems);
				}


				if(processType.equals(4)){
					//取消订单
					if(from == 1){
						//tb_order
						systemOrder.setPayStatus(-2);
						systemOrder.setOrderStatus(OrderStatus.ORDER_STATUS_CANCELED);
						systemOrder.setGmtUpdate(LocalDateTime.now());
						systemOrderMapper.update(systemOrder);

						if(systemOrderExt != null){
							systemOrderExt.setSysOrderStatus(OrderStatus.ORDER_STATUS_CANCEL);
							systemOrderExt.setConfirmFlag(1);
							systemOrderExt.setGmtUpdate(LocalDateTime.now());
							systemOrderExtMapper.update(systemOrderExt);
						}
						//当前订单获取到的优惠卷
						try {
							DynamicDatasourceHolder.setDataSource("site");
                            List<SitePromotionCouponRecord> sitePromotionCouponRecords = sitePromotionCouponRecordMapper.selectListByQuery(new QueryWrapper().eq("status", 1).eq("fromOrderId", orderId));
							if(sitePromotionCouponRecords != null && !sitePromotionCouponRecords.isEmpty()) {
								for (SitePromotionCouponRecord couponRecord : sitePromotionCouponRecords) {
									if(couponRecord.getUseStatus().equals(9)) {
										//暂不处理
									}else {
                                        sitePromotionCouponRecordMapper.delete(couponRecord);
									}
								}
							}
						} finally {
                            DynamicDatasourceHolder.removeDataSource();
						}
					}else{
						//site_order
                        SystemOrderTrackingRecord systemOrderTrackingRecord = new SystemOrderTrackingRecord();
                        systemOrderTrackingRecord.setOrderId(orderId);
                        systemOrderTrackingRecord.setStatusCode(OrderTrackStatus.ORDER_STATUS_ORDER_REMINDER);
						systemOrderTrackingRecord.setGmtCreate(LocalDateTime.now());
						systemOrderTrackingRecordMapper.insertSelective(systemOrderTrackingRecord);
					}
					SystemOrderTrackingRecord orderTrack = getOrderTrackRecord(orderId, OrderTrackStatus.ORDER_STATUS_CLOSED_REMINDER);
					if(orderTrack == null){
						orderTrack = new SystemOrderTrackingRecord();
						orderTrack.setOrderId(orderId);
						orderTrack.setStatusCode(OrderTrackStatus.ORDER_STATUS_CLOSED_REMINDER);
						orderTrack.setGmtCreate(LocalDateTime.now());
						systemOrderTrackingRecordMapper.insertSelective(orderTrack);
					}

					//修改商品状态
					List<SystemOrderItems> itemsList = systemOrderItemsMapper.selectListByQuery(new QueryWrapper().eq("orderId", orderId));
					if(!itemsList.isEmpty()){
						for (SystemOrderItems item : itemsList) {
							item.setSysStatus(OrderStatus.ORDER_STATUS_CANCEL);
							item.setDeleteFlag(1);
							item.setGmtUpdate(LocalDateTime.now());
							systemOrderItemsMapper.update(item);
						}
					}else{
						try {
							//site_order_items
							DynamicDatasourceHolder.setDataSource("site");
							List<SiteOrderItems> siteOrderItems = siteOrderItemsMapper.selectListByQuery(new QueryWrapper().eq("orderId", orderId));
							if(!siteOrderItems.isEmpty()){
								for (SiteOrderItems item : siteOrderItems) {
									item.setDeleteFlag(1);
									item.setGmtUpdate(LocalDateTime.now());
									siteOrderItemsMapper.update(item);
								}
							}
						} finally {
							DynamicDatasourceHolder.removeDataSource();
						}
					}

				}
			}
			return true;
		});
		//保存
		if(id != null){
			return id;
		}else{
			return systemOrderAftersaleMapper.selectOneByQuery(new QueryWrapper()
					.eq("orderId", orderId)
					.eq("itemId", id)
					.eq("itemSkuId", itemSkuId)
					.orderBy("id desc")
					.limit(1)).getId();
		}
	}

	@Override
	public void updateAftersaleExpress(String siteCode,String languageCode,Integer itemId,Integer itemSkuId,String orderId,Integer id,String memberId,String expressNo,String expressName){
		SystemOrderAftersale aftersaleInfo = getAftersale(siteCode,itemId,itemSkuId,orderId,id,memberId);
		if(aftersaleInfo != null){
			if(aftersaleInfo.getStatus().equals(2) || aftersaleInfo.getStatus().equals(3)){
				//审核通过
				if(aftersaleInfo.getProcessType().equals(1) || aftersaleInfo.getProcessType().equals(3)){
					//退货  换货
					aftersaleInfo.setExpressNo(expressNo);
					aftersaleInfo.setExpressName(expressName);
					aftersaleInfo.setExpressStatus("SHIPPED");
					aftersaleInfo.setStatus(3);
					aftersaleInfo.setGmtUpdate(LocalDateTime.now());
					aftersaleInfo.setDeliverTime(LocalDateTime.now());
					systemOrderAftersaleMapper.update(aftersaleInfo);
				}
			}
		}
	}

	public SystemOrderAftersale getAftersale(String siteCode, Integer itemId, Integer itemSkuId, String orderId, Integer id, String memberId){
		return systemOrderAftersaleMapper.selectOneByQuery(new QueryWrapper()
				.eq("id", id)
				.eq("siteCode", siteCode)
				.eq("orderId", orderId)
				.eq("itemId", itemId)
				.eq("itemSkuId", itemSkuId)
				.eq("memberId", memberId));
	}

	@Override
	public void cancelAftersale(String siteCode,Integer itemId,Integer itemSkuId,String orderId,Integer id,String memberId){
		Db.tx(()->{
			SystemOrderAftersale record = getAftersale(siteCode, itemId, itemSkuId, orderId, id, memberId);
			if(record != null){
				record.setStatus(-1);
				record.setGmtUpdate(LocalDateTime.now());
				systemOrderAftersaleMapper.update(record);
			}
			SystemOrderExt systemOrderExt = systemOrderExtMapper.selectOneByQuery(new QueryWrapper().eq("orderId", orderId));
			if(systemOrderExt != null){
				systemOrderExt.setProcessType(0);
				systemOrderExt.setGmtUpdate(LocalDateTime.now());
				systemOrderExtMapper.update(systemOrderExt);
			}
			SystemOrderItems systemOrderItems = systemOrderItemsMapper.selectOneByQuery(new QueryWrapper().eq("orderId", orderId).eq("itemId", itemId));
			if(systemOrderItems != null){
				systemOrderItems.setProcessType(0);
				systemOrderItems.setGmtUpdate(LocalDateTime.now());
				systemOrderItemsMapper.update(systemOrderItems);
			}
			return true;
		});
	}

	@Override
	public void deleteAftersaleImage(String siteCode,Integer itemId,Integer itemSkuId,String orderId,Integer id,String memberId,String imgProof){
		SystemOrderAftersale record = getAftersale(siteCode, itemId, itemSkuId, orderId, id, memberId);
		if(record != null){
			String imgProofs = record.getImgProof();
			if(StringUtils.isNotBlank(imgProofs)){
				imgProofs = imgProofs.replace(imgProof, "");
				if(imgProofs.contains(",")){
					StringBuilder builder = new StringBuilder();
					String[] imgsplit = imgProofs.split(",");
					for (int i = 0; i < imgsplit.length; i++) {
						if(imgsplit[i] != null && !"".equals(imgsplit[i])){
							builder.append(imgsplit[i]);
							if(i+1 != imgsplit.length){
								builder.append(",");
							}
						}
					}
					record.setImgProof(builder.toString());
				}else{
					record.setImgProof(imgProofs);
				}

				record.setGmtUpdate(LocalDateTime.now());
				systemOrderAftersaleMapper.update(record);
			}
		}
	}

	@Override
	public String getOrderItemsStatus(String siteCode, String orderId, String memberId) {
		String sysStatus = "UNPURCHASE";
		List<SystemOrderItems> itemsList = systemOrderItemsMapper.selectListByQuery(new QueryWrapper().eq("orderId", orderId));
		if(!itemsList.isEmpty()){
			for (SystemOrderItems orderItem : itemsList) {
				Integer itemId = orderItem.getItemId();
				Integer itemSkuId = orderItem.getItemSkuId();
				SystemOrderAftersale systemOrderAftersale = systemOrderAftersaleMapper.selectOneByQuery(new QueryWrapper()
						.eq("orderId", orderId)
						.eq("itemId", itemId)
						.eq("itemSkuId", itemSkuId)
						.eq("memberId", memberId)
						.in("status", List.of(0, 2, -2, 3, 4)));
				if(systemOrderAftersale != null){
					//存在售后处理
					return OrderStatus.ORDER_STATUS_PROCESSING;
				}else{
					if(!orderItem.getSysStatus().equals(sysStatus)){
						return orderItem.getSysStatus();
					}
				}
			}
		}
		return sysStatus;
	}

	@Override
	public String getOrderStatus(String siteCode, String orderId, String memberId) {
		SystemOrder systemOrder = systemOrderMapper.selectOneById(orderId);
		String orderStatus = null;
		if(systemOrder == null || StringUtils.isBlank(systemOrder.getOrderStatus())){
			try {
				DynamicDatasourceHolder.setDataSource("site");
				SiteOrder siteOrder = siteOrderMapper.selectOneById(orderId);
				if (siteOrder != null) {
					orderStatus = siteOrder.getOrderStatus();
				}

			} finally {
				DynamicDatasourceHolder.removeDataSource();
			}
		}else{
			orderStatus = systemOrder.getOrderStatus();
			if(orderStatus.equals(OrderStatus.ORDER_STATUS_RECEIVED)){
				//已收货可7天退换货
				LocalDateTime gmtUpdate = systemOrder.getGmtUpdate();
				int leftSeconds = DateUtil.getLeftSeconds(gmtUpdate, 7*24*60*60*1000);
				if(leftSeconds <= 0){
					//超时
					orderStatus = OrderStatus.ORDER_STATUS_TIMEOUTCLOSED;
				}
			}
		}

		return orderStatus;
	}

	@Override
	public int getOrderItemSkuNumByTime(Integer itemSkuId, String memberId, Date beginTime, Date endTime) {
		long itemSkuNum = systemOrderItemsMapper.selectCountByQuery(new QueryWrapper()
				.in("sysStatus", List.of("UNPURCHASE", "PURCHASING"))
				.eq("memberId", memberId)
				.eq("itemSkuId", itemSkuId)
				.between("gmtCreate", LocalDateTime.ofInstant(beginTime.toInstant(), ZoneId.systemDefault()), LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault())));
        return (int) itemSkuNum;
	}

	public List<String> getTHProvinceList(String zipcode){
		try {
			DynamicDatasourceHolder.setDataSource("site");
			List<String> provinceList = new ArrayList<>();
			List<SiteAreaTh> siteAreaThs = siteAreaThMapper.selectListByQuery(new QueryWrapper().select("DISTINCT province").eq("status", 1).eq("zipcode", zipcode));
			if (siteAreaThs != null) {
				provinceList = siteAreaThs.stream().map(SiteAreaTh::getProvince).toList();
			}
			return provinceList;
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	public List<String> getTHCityList(String zipcode,String province){
		try {
			DynamicDatasourceHolder.setDataSource("site");
			List<String> cityList = new ArrayList<>();
			if(province == null) {
				List<SiteAreaTh> siteAreaThs = siteAreaThMapper.selectListByQuery(new QueryWrapper().select("distinct city").eq("status", 1).eq("zipcode", zipcode));
				if (siteAreaThs != null) {
					cityList = siteAreaThs.stream().map(SiteAreaTh::getCity).toList();
				}
			} else {
				List<SiteAreaTh> siteAreaThs = siteAreaThMapper.selectListByQuery(new QueryWrapper()
						.select("distinct city")
						.eq("status", 1)
						.eq("province", province)
						.eq("zipcode", zipcode));
				if (siteAreaThs != null) {
					cityList = siteAreaThs.stream().map(SiteAreaTh::getCity).toList();
				}
			}
			return cityList;
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	public List<String> getGRProvinceList(String zipcode){
		try {
			DynamicDatasourceHolder.setDataSource("site");
			List<String> provinceList = new ArrayList<>();
            List<SiteAreaGr> siteAreaGrs = siteAreaGrMapper.selectListByQuery(new QueryWrapper()
                    .select("distinct province")
                    .eq("status", 1)
                    .eq("zipcode", zipcode));
            if (siteAreaGrs != null) {
                provinceList = siteAreaGrs.stream().map(SiteAreaGr::getProvince).toList();
            }
			return provinceList;
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	public List<String> getGRCityList(String zipcode,String province){
		try {
			DynamicDatasourceHolder.setDataSource("site");
			List<String> cityList = new ArrayList<>();
			if(province == null){
				List<SiteAreaGr> siteAreaGrs = siteAreaGrMapper.selectListByQuery(new QueryWrapper()
						.select("distinct city")
						.eq("status", 1)
						.eq("zipcode", zipcode));
				if (siteAreaGrs != null) {
					cityList = siteAreaGrs.stream().map(SiteAreaGr::getCity).toList();
				}
			}else{
				List<SiteAreaGr> siteAreaGrs = siteAreaGrMapper.selectListByQuery(new QueryWrapper()
						.select("distinct city")
						.eq("status", 1)
						.eq("province", province)
						.eq("zipcode", zipcode));
				if (siteAreaGrs != null) {
					cityList = siteAreaGrs.stream().map(SiteAreaGr::getCity).toList();
				}
			}
			return cityList;
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	@Override
	public String getOneMemberIdByPhone(String siteCode, String mobile){
		return systemOrderMapper.selectOneMemberIdByPhone(siteCode, mobile);
	}

	public String getRecentOrderByPhone(String siteCode, String mobile, Date date){
		return systemOrderMapper.selectRecentOrderByPhone(siteCode, mobile, "SUBMITED", LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
	}

	@Override
	public Ret handleOrderCouponByPhone(String siteCode, String mobile, String memberId, Integer couponId, String reason, String remark,Integer optUserId){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Date startOfDay = calendar.getTime();
		//tb库 查询当天最新的一个初始订单
		String orderId = getRecentOrderByPhone(siteCode, mobile, startOfDay);
		if (orderId == null) {
			return Ret.fail("message", "当天没有符合的订单").set("type", "No Order");
		}
		SystemOrder oriSystemOrder = systemOrderMapper.selectOneById(orderId);
		//查询新人优惠卷
		SitePromotionCouponRecord coupon;
		try {
			coupon = sitePromotionCouponRecordMapper.selectOneByQuery(new QueryWrapper()
					.eq("status", 1)
					.eq("useStatus", 0)
					.eq("fromPlatformId", 3)
					.eq("couponId", couponId)
					.eq("memberId", memberId));;
		} finally {

		}
		if (coupon == null || oriSystemOrder == null) {
			return Ret.fail("message", "新人优惠卷不存在/不可使用/已使用").set("type", "No Coupon");
		}

		boolean result = Db.tx(()->{

			//处理商品优惠
			handCouponPriceOrderItems(coupon, oriSystemOrder, orderId, 1, startOfDay);

			//处理订单优惠
			oriSystemOrder.setTotalCouponPrice(coupon.getPromoPrice());
			BigDecimal totalPayPrice = oriSystemOrder.getTotalSalePrice().add(oriSystemOrder.getTotalShippingPrice())
					.subtract(oriSystemOrder.getTotalCouponPrice()).subtract(oriSystemOrder.getTotalPromoPrice())
					.subtract(oriSystemOrder.getTotalPointPrice()).subtract(oriSystemOrder.getTotalDepositPrice());
			if(oriSystemOrder.getTotalPayPrice().compareTo(BigDecimal.ZERO) < 0) {
				totalPayPrice = BigDecimal.ZERO;
			}
			oriSystemOrder.setTotalPayPrice(totalPayPrice);
			oriSystemOrder.setGmtUpdate(LocalDateTime.now());
			systemOrderMapper.update(oriSystemOrder);

			SystemOrderExt systemOrderExt = systemOrderExtMapper.selectOneByQuery(new QueryWrapper().eq("orderId", orderId));
			String sysOrderStatus = null;
			if (systemOrderExt == null || StringUtils.isBlank(systemOrderExt.getSysOrderStatus())) {
				sysOrderStatus = oriSystemOrder.getOrderStatus();
			} else {
				sysOrderStatus = systemOrderExt.getSysOrderStatus();
			}

			//更新优惠卷状态
			coupon.setOrderId(orderId);
			coupon.setUseStatus(9);
			coupon.setUsedTime( LocalDateTime.ofInstant(startOfDay.toInstant(), ZoneId.systemDefault()));
			try {
				DynamicDatasourceHolder.setDataSource("site");
				sitePromotionCouponRecordMapper.update(coupon);
			} finally {
				DynamicDatasourceHolder.removeDataSource();
			}

			//添加日志记录
			saveOrderLog(memberId, mobile, orderId, sysOrderStatus, sysOrderStatus, oriSystemOrder.getOrderStatus(), reason, remark, optUserId);
			return true;
		});
		if (result) {
			return Ret.ok("message", "Success").set("orderNo", oriSystemOrder.getOrderNo());
		} else {
			return Ret.fail("message", "Fail").set("type", "Error");
		}

	}

	@Override
	public void saveOrderLog(String memberId, String mobile,String orderId, String statusFlag, String sysOrderStatus, String orderStatus,
			String reason, String remark,Integer optUserId){
        SystemLogOrder orderLog = new SystemLogOrder();
		orderLog.setOrderId(orderId);
		orderLog.setStatusFlag(statusFlag);
		orderLog.setOptUserId(optUserId);
		orderLog.setReason(reason);
		orderLog.setRemark(remark);
		orderLog.setSysOrderStatus(sysOrderStatus);
		orderLog.setOrderStatus(orderStatus);
		orderLog.setGmtCreate(LocalDateTime.now());
        systemLogOrderMapper.insertSelective(orderLog);
	}

	private void handCouponPriceOrderItems(SitePromotionCouponRecord coupon, SystemOrder oriSystemOrder, String  orderId, Integer userId, Date date) {
		//获取订单商品列表
		List<SystemOrderItems> itemsList = getListByOrderId(orderId);
		BigDecimal totalCouponPrice = coupon.getPromoPrice();
		BigDecimal leftCouponPrice = totalCouponPrice;
		BigDecimal currentCouponPrice = BigDecimal.ZERO;
		int itemListSizeIndex = itemsList.size()-1;
		int i = 0;
		for(SystemOrderItems orderItem : itemsList) {
			//保存备份
            SystemOrderItemsBackup itemRecord = new SystemOrderItemsBackup();
            itemRecord.setOperator(userId);
			itemRecord.setGmtUpdate(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));//备份添加时间
            systemOrderItemsBackupMapper.insertSelective(itemRecord);
			currentCouponPrice = BigDecimal.ZERO;
			if(i!=itemListSizeIndex) {
				BigDecimal itemRate = orderItem.getSubTotalSalePrice().divide(oriSystemOrder.getTotalSalePrice(), 2, BigDecimal.ROUND_HALF_UP);
				currentCouponPrice = totalCouponPrice.multiply(itemRate).setScale(0, BigDecimal.ROUND_DOWN);
				orderItem.setSubTotalCouponPrice(currentCouponPrice);
				orderItem.setSubTotalPayPrice(orderItem.getSubTotalSalePrice().subtract(currentCouponPrice));
				leftCouponPrice = leftCouponPrice.subtract(currentCouponPrice);
			}else {
				orderItem.setSubTotalCouponPrice(leftCouponPrice);
				orderItem.setSubTotalPayPrice(orderItem.getSubTotalSalePrice().subtract(leftCouponPrice));
			}
			orderItem.setSubTotalPayPrice(orderItem.getSubTotalPayPrice()
					.subtract(orderItem.getSubTotalPromoPrice())
					.subtract(orderItem.getSubTotalReducePrice())
					.subtract(orderItem.getSubTotalPointPrice())
					.subtract(orderItem.getSubTotalWalletPrice()));
			if(orderItem.getSubTotalPayPrice().compareTo(BigDecimal.ZERO) <= 0) {
				orderItem.setSubTotalPayPrice(BigDecimal.ZERO);
			}
			orderItem.setGmtUpdate(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
			systemOrderItemsMapper.update(orderItem);
			i++;
		}

	}

	public List<SystemOrderItems> getListByOrderId(String orderId) {
		return systemOrderItemsMapper.selectListByQuery(new QueryWrapper()
				.ne("deleteFlag", 1)
				.ne("sysStatus", "CANCEL")
				.eq("orderId", orderId));
	}
}


