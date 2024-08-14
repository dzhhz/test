package com.feilu.api.controller.system;

import com.feilu.api.common.entity.Ret;
import com.feilu.api.common.service.RecommendService;
import com.feilu.api.common.annotation.ValidateRequestData;
import com.feilu.api.service.system.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/item")
public class ItemController {

	private ItemService itemService;

	@Autowired
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	/**
	 * 获取商品详情页的推荐商品
	 */
	@PostMapping("recommendItemListForDetail")
	@ValidateRequestData
	public Ret recommendItemListForDetail(@RequestBody Map<String,Object> requestParam) {
		Map<String, Object> result = itemService.recommendItemListByType(requestParam.get("requestData").toString(), RecommendService.DETAIL);
		return Ret.ok("result", result);
	}

	/**
	 * 获取首页的推荐商品
	 */
	@PostMapping("recommendItemListForHome")
	@ValidateRequestData
	public Ret recommendItemListForHome(@RequestBody Map<String,Object> requestParam) {
		Map<String, Object> result = itemService.recommendItemListByType(requestParam.get("requestData").toString(), RecommendService.HOME);
		return Ret.ok("result", result);
	}

	/**
	 * 获取我的页面的推荐商品
	 */
	@PostMapping("recommendItemListForMine")
	@ValidateRequestData
	public Ret recommendItemListForMine(@RequestBody Map<String,Object> requestParam) {
		Map<String, Object> result = itemService.recommendItemListByType(requestParam.get("requestData").toString(), RecommendService.MINE);
		return  Ret.ok("result", result);
	}


	/**
	 * 获取下单成功页的推荐商品
	 */
	@PostMapping("recommendItemListForOrderSuccess")
	@ValidateRequestData
	public Ret recommendItemListForOrderSuccess(@RequestBody Map<String,Object> requestParam) {
		Map<String, Object> result = itemService.recommendItemListByType(requestParam.get("requestData").toString(), RecommendService.ORDER_SUCCESS);
		return Ret.ok("result", result);
	}
}
