package com.feilu.api.service.system.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.feilu.api.common.entity.Ret;
import com.feilu.api.dao.system.entity.SystemMemberFeedback;
import com.feilu.api.dao.system.entity.SystemMemberWallet;
import com.feilu.api.dao.system.entity.SystemMemberWalletRecord;
import com.feilu.api.dao.system.entity.SystemOrder;
import com.feilu.api.dao.system.mapper.SystemMemberFeedbackMapper;
import com.feilu.api.dao.system.mapper.SystemMemberWalletMapper;
import com.feilu.api.dao.system.mapper.SystemMemberWalletRecordMapper;
import com.feilu.api.dao.system.mapper.SystemOrderMapper;
import com.feilu.api.dao.website.entity.SiteBaseSitecode;
import com.feilu.api.service.site.BaseSiteService;
import com.feilu.api.service.system.MemberService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dzh
 */
@Service
public class MemberServiceImpl implements MemberService {

	private BaseSiteService baseSiteService;

    private SystemMemberFeedbackMapper systemMemberFeedbackMapper;

	private SystemMemberWalletMapper systemMemberWalletMapper;

	private SystemMemberWalletRecordMapper systemMemberWalletRecordMapper;

    private SystemOrderMapper systemOrderMapper;

	@Autowired
	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	@Autowired
	public void setSystemMemberFeedbackMapper(SystemMemberFeedbackMapper systemMemberFeedbackMapper) {
		this.systemMemberFeedbackMapper = systemMemberFeedbackMapper;
	}

	@Autowired
	public void setSystemMemberWalletMapper(SystemMemberWalletMapper systemMemberWalletMapper) {
		this.systemMemberWalletMapper = systemMemberWalletMapper;
	}

	@Autowired
	public void setSystemMemberWalletRecordMapper(SystemMemberWalletRecordMapper systemMemberWalletRecordMapper) {
		this.systemMemberWalletRecordMapper = systemMemberWalletRecordMapper;
	}

	@Autowired
	public void setSystemOrderMapper(SystemOrderMapper systemOrderMapper) {
		this.systemOrderMapper = systemOrderMapper;
	}

	/**
	 * 获取意见反馈列表
	 * @param siteCode 站点
	 * @param memberId 会员id
	 * @param pageNumber 页码数
	 * @param pageSize 分页大小
	 * @return 意见反馈列表
	 */
	@Override
	public Page<Map<String, Object>> getFeedbacks(String siteCode, String memberId, int pageNumber, int pageSize){
		Page<Map<String, Object>> result = new Page<>();
		Page<SystemMemberFeedback> page = systemMemberFeedbackMapper.paginate(pageNumber, pageSize, new QueryWrapper().eq("siteCode", siteCode).eq("memberId", memberId));

		// 创建结果 Page 对象
		result.setPageNumber(page.getPageNumber());
		result.setPageSize(page.getPageSize());
		result.setTotalPage(page.getTotalPage());
		result.setTotalRow(page.getTotalRow());

		// 转换数据列表
		SiteBaseSitecode baseSite = baseSiteService.getBaseSite(siteCode);
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern(baseSite.getDateTimeFormat());
		List<Map<String, Object>> records = page.getRecords().stream()
				.map(feedback -> {
					Map<String, Object> map = new HashMap<>();
					map.put("content", feedback.getContent());
					map.put("reply", feedback.getReply());
					map.put("gmtCreate", feedback.getGmtCreate().format(pattern));
					return map;
				})
				.collect(Collectors.toList());

		result.setRecords(records);

		return result;
	}

	/**
	 * 保存意见反馈列表
	 * @param memberId 会员id
	 * @param siteCode 站点
	 * @param opinion 文字内存
	 * @param imageList 图片列表
	 * @return 保存结果
	 */
	@Override
	public Ret saveFeedback(String memberId, String siteCode, String opinion, JSONArray imageList){
		SystemMemberFeedback systemMemberFeedback = new SystemMemberFeedback();
		systemMemberFeedback.setSiteCode(siteCode);
		systemMemberFeedback.setMemberId(memberId);
		Map<String, Object> content = new HashMap<>();
		content.put("content", opinion);
		content.put("images", imageList);
		systemMemberFeedback.setContent(JSON.toJSONString(content));
		systemMemberFeedback.setGmtCreate(LocalDateTime.now());
		systemMemberFeedbackMapper.insertSelective(systemMemberFeedback);
		return Ret.ok("msg", "保存成功!");
	}


	/**
	 * 获取钱包总额
	 * @param siteCode 站点
	 * @param currencyCode 货币代码
	 * @param memberId 会员id
	 * @return 钱包总额
	 */
	@Override
	public BigDecimal getTotalWalletAmount(String siteCode, String currencyCode, String memberId){
		SystemMemberWallet systemMemberWallet = systemMemberWalletMapper.selectOneByQuery(new QueryWrapper()
				.eq("currencyCode", currencyCode)
				.eq("memberId", memberId));
		BigDecimal totalWalletAmount = BigDecimal.ZERO;
		if(systemMemberWallet != null && systemMemberWallet.getTotalWalletAmount() != null){
			totalWalletAmount = systemMemberWallet.getTotalWalletAmount();
		}
		return totalWalletAmount;

	}

	/**
	 * 分页获取钱包记录
	 * @param siteCode 站点
	 * @param currencyCode 货币代码
	 * @param memberId 会员id
	 * @param pageNumber 页码数
	 * @param pageSize 分页大小
	 * @return 分页结果
	 */
	@Override
	public Page<Map<String, Object>> pageWalletRecordList(String siteCode, String currencyCode, String memberId, int pageNumber, int pageSize){
		List<Map<String, Object>> records = systemMemberWalletRecordMapper.pageMemberWalletRecord(memberId, siteCode, currencyCode, pageSize, (pageNumber - 1) * pageSize);
		Integer count = systemMemberWalletRecordMapper.countMemberWalletRecord(memberId, siteCode, currencyCode);
		Page<Map<String, Object>> result = new Page<>();
		result.setPageNumber(pageNumber);
		result.setPageSize(pageSize);
		result.setTotalPage((int) Math.ceil((double) count / pageSize));
		result.setTotalRow(count);

		if(!records.isEmpty()){
			SiteBaseSitecode baseSite = baseSiteService.getBaseSite(siteCode);
			DateTimeFormatter pattern = DateTimeFormatter.ofPattern(baseSite.getDateTimeFormat());
			for (Map<String, Object> record : records) {
				record.put("gmtCreate", ((LocalDateTime)record.get("gmtCreate")).format(pattern));
				record.put("amountText", "" + record.get("optType") + record.get("currencySymbol") + record.get("amount"));
				Object orderIdObj = record.get("orderId");
				if (orderIdObj != null && StringUtils.isNotBlank(orderIdObj.toString())) {
                    SystemOrder systemOrder = systemOrderMapper.selectOneById(orderIdObj.toString());
                    if (systemOrder != null) {
                        record.put("orderId", systemOrder.getOrderNo());
                    }
				}
			}
		}
		result.setRecords(records);
		return result;
	}

	/**
	 * 保存钱包记录
	 * @param memberId 会员id
	 * @param siteCode 站点
	 * @param currencyCode 货币代码
	 * @param amount 操作钱数
	 * @param optType 操作类型 +  or -
	 * @param type 类型
	 * @param status 状态
	 * @param orderId 订单id
	 * @return 操作结果
	 */
	@Override
	public Ret saveWalletRecord(String memberId, String siteCode, String currencyCode,BigDecimal amount,String optType,String type,String status,String orderId){
        SystemMemberWalletRecord systemMemberWalletRecord = new SystemMemberWalletRecord();
        systemMemberWalletRecord.setSiteCode(siteCode);
        systemMemberWalletRecord.setCurrencyCode(currencyCode);
        systemMemberWalletRecord.setMemberId(memberId);
        systemMemberWalletRecord.setAmount(amount);
        systemMemberWalletRecord.setOptType(optType);
        systemMemberWalletRecord.setType(type);
        systemMemberWalletRecord.setOrderId(orderId);
        systemMemberWalletRecord.setStatus(status);
        systemMemberWalletRecord.setGmtCreate(LocalDateTime.now());
		Db.tx(() -> {
			systemMemberWalletRecordMapper.insertSelective(systemMemberWalletRecord);
			updateWalletAmount(memberId, currencyCode, optType, amount);
			return true;
		});
		return Ret.ok("message", "保存成功!");
	}

	/**
	 * 更新钱包总金额
	 * @param memberId 会员id
	 * @param currencyCode 货币代码
	 * @param optType 操作类型
	 * @param amount 金额
	 */
	@Override
	public void updateWalletAmount(String memberId,String currencyCode,String optType, BigDecimal amount){
		SystemMemberWallet systemMemberWallet = systemMemberWalletMapper.selectOneByQuery(new QueryWrapper().eq("memberId", memberId).eq("currencyCode", currencyCode));
		if(systemMemberWallet != null){
			if("+".equals(optType)){
				systemMemberWallet.setTotalWalletAmount(systemMemberWallet.getTotalWalletAmount().add(amount));
			}
			if("-".equals(optType)){
				// 检查扣减金额是否大于余额
				if (systemMemberWallet.getTotalWalletAmount().compareTo(amount) < 0) {
					// 余额不足
					throw new RuntimeException("余额不足，无法扣减");
				} else {
					// 扣减金额大于余额
					systemMemberWallet.setTotalWalletAmount(systemMemberWallet.getTotalWalletAmount().subtract(amount));
				}
			}
			systemMemberWallet.setGmtUpdate(LocalDateTime.now());
			systemMemberWalletMapper.update(systemMemberWallet);
		}
	}

	/**
	 * 更新钱包操作记录
	 * @param orderId 订单id
	 * @param status 状态
	 */
	@Override
	public void updateWalletRecord(String orderId, String status){
		SystemMemberWalletRecord systemMemberWalletRecord = systemMemberWalletRecordMapper.selectOneByQuery(new QueryWrapper().eq("orderId", orderId));
		if(systemMemberWalletRecord != null){
			if (StringUtils.isNotBlank(status)) {
				systemMemberWalletRecord.setStatus(status);
			}
			systemMemberWalletRecord.setGmtUpdate(LocalDateTime.now());
			systemMemberWalletRecordMapper.update(systemMemberWalletRecord);
		}
	}

}
