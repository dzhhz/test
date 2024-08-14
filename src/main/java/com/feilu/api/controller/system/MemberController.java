package com.feilu.api.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feilu.api.common.entity.Ret;
import com.feilu.api.common.annotation.ValidateRequestData;
import com.feilu.api.dao.website.entity.SiteBaseCurrency;
import com.feilu.api.service.site.BaseSiteService;
import com.feilu.api.service.system.MemberService;
import com.mybatisflex.core.paginate.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("member")
public class MemberController {


	private MemberService memberService;
	private BaseSiteService baseSiteService;

	@Autowired
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	@Autowired
	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	/**
	 * 意见反馈列表
	 */
	@PostMapping("getFeedbackList")
	@ValidateRequestData
	public Ret getFeedbackList(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String memberId = map.getString("memberId");
		Integer pageNumber = map.getInteger("pageNumber");
		Integer pageSize = map.getInteger("pageSize");
		Page<Map<String, Object>> result = memberService.getFeedbacks(siteCode, memberId, pageNumber, pageSize);
        return Ret.ok("result", result);
	}

	/**
	 * 意见反馈保存
	 */
	@PostMapping("saveFeedback")
	@ValidateRequestData
	public Ret saveFeedback(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String memberId = map.getString("memberId");
		String siteCode = map.getString("siteCode");
		String opinion = map.getString("opinion");
		JSONArray imageList = map.getJSONArray("imageList");

		return memberService.saveFeedback(memberId,siteCode,opinion,imageList);
	}

	/**
	 * 钱包金额
	 */
	@PostMapping("getWalletAmount")
	@ValidateRequestData
	public Ret getWalletAmount(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String currencyCode = map.getString("currencyCode");
		String memberId = map.getString("memberId");
		BigDecimal totalWalletAmount = memberService.getTotalWalletAmount(siteCode, currencyCode, memberId);
		SiteBaseCurrency siteBaseCurrency = baseSiteService.getBaseCurrency(currencyCode);
		Map<String, Object> result = new HashMap<>();
		result.put("totalWalletAmount", totalWalletAmount);
		result.put("currencySymbol", siteBaseCurrency.getCurrencySymbol());
		return  Ret.ok("result", result);
	}

	/**
	 * 钱包记录列表
	 */
	@PostMapping("getWalletList")
	@ValidateRequestData
	public Ret getWalletList(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String currencyCode = map.getString("currencyCode");
		String memberId = map.getString("memberId");
		Integer pageNumber = map.getInteger("pageNumber");
		Integer pageSize = map.getInteger("pageSize");
		Page<Map<String, Object>> result = memberService.pageWalletRecordList(siteCode, currencyCode, memberId, pageNumber, pageSize);
		return Ret.ok("result", result);
	}

	/**
	 * 钱包记录保存,并更新钱包金额
	 */
	@PostMapping("saveWalletRecord")
	@ValidateRequestData
	public Ret saveWalletRecord(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String memberId = map.getString("memberId");
		String siteCode = map.getString("siteCode");
		String currencyCode = map.getString("currencyCode");
		String amountStr = map.getString("amount");
		BigDecimal amount = amountStr != null ? new BigDecimal(amountStr) : null;
		String optType = map.getString("optType");
		String type = map.getString("type");
		String orderId = map.getString("orderId");
		String status = map.getString("status");

		return memberService.saveWalletRecord(memberId,siteCode,currencyCode,amount,optType,type,status,orderId);
	}

	/**
	 * 钱包金额更新
	 */
	@PostMapping("updateWalletAmount")
	@ValidateRequestData
	public Ret updateWalletAmount(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String memberId = map.getString("memberId");
		String currencyCode = map.getString("currencyCode");
		String amountStr = map.getString("amount");
		BigDecimal amount = amountStr != null ? new BigDecimal(amountStr) : null;
		String optType = map.getString("optType");

		memberService.updateWalletAmount(memberId,currencyCode,optType,amount);
		return Ret.ok();
	}
}
