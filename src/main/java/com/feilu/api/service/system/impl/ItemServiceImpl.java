package com.feilu.api.service.system.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feilu.api.common.service.RecommendService;
import com.feilu.api.dao.system.entity.SystemProduct;
import com.feilu.api.dao.system.mapper.SystemProductMapper;
import com.feilu.api.service.system.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author dzh
 */
@Service
public class ItemServiceImpl implements ItemService {

	private RecommendService recommendService;
    private SystemProductMapper systemProductMapper;

    @Autowired
	public void setRecommendService(RecommendService recommendService) {
		this.recommendService = recommendService;
	}

	@Autowired
	public void setSystemProductMapper(SystemProductMapper systemProductMapper) {
		this.systemProductMapper = systemProductMapper;
	}

	/**
	 * 根据页面类型推荐商品
	 * @param requestData 请求数据
	 * @param type 页面类型
	 * @return 商品列表
	 */
	@Override
	public Map<String, Object> recommendItemListByType(String requestData, String type) {
		JSONObject map = JSON.parseObject(requestData);
		String siteCode = map.getString("siteCode");
		String deviceId = map.getString("deviceId");
		Integer pageNumber = map.getInteger("pageNumber");
		Integer pageSize = map.getInteger("pageSize");
		String categoryIds = map.getString("categoryIds");
		Integer itemId = map.getInteger("itemId");
		Integer productId = map.getInteger("productId");
		String itemIdsStr = map.getString("itemIdsStr");
		// 初始化 sexFlag  ageGroupFlag  seasonFlag
		Integer sexFlag = null;
		String ageGroupFlag = null;
		if (productId != null) {
            SystemProduct systemProduct = systemProductMapper.selectOneById(productId);
			if (systemProduct != null) {
				ageGroupFlag = systemProduct.getAgeGroupFlag();
				sexFlag = systemProduct.getSexFlag();
			}
		}

		Map<Integer, String> recommendSourceMap = new HashMap<>();
		Set<Integer> itemIdSet = new HashSet<>();
		// 根据不同类型推荐商品  都不满足直接去es查
		switch (type) {
			case RecommendService.HOME:
				recommendItemListForHome(itemIdSet, recommendSourceMap, siteCode, deviceId, pageNumber);
				break;
			case RecommendService.DETAIL:
				recommendItemListForDetail(itemIdSet, recommendSourceMap, siteCode, deviceId, itemId, categoryIds, sexFlag, ageGroupFlag, pageNumber);
				break;
			case RecommendService.MINE:
				recommendItemListForMine(itemIdSet, recommendSourceMap, siteCode, deviceId, pageNumber);
				break;
			case RecommendService.ORDER_SUCCESS:
				recommendItemListForOrderSuccess(itemIdSet, recommendSourceMap, siteCode, deviceId, itemIdsStr, pageNumber, pageSize);
				break;
		}
		Map<String, Object> result = new HashMap<>();
		result.put("itemIdList", new ArrayList<>(itemIdSet));
		result.put("recommendSourceMap", recommendSourceMap);
		return result;
	}

	public void recommendItemListForHome(Set<Integer> itemIdSet, Map<Integer, String> recommendSourceMap, String siteCode, String deviceId, Integer pageNumber) {
		int recommendSize = 5;

		//	根据个人推荐
		List<Integer> itemIds = recommendService.recommendItemByUser(deviceId,  100, siteCode);
		handleItemIdList(itemIds, itemIdSet, recommendSourceMap, pageNumber, recommendSize, RecommendService.MODEL);

		// 新品
		List<Integer> newItemList = recommendService.recommendNewItem(siteCode, pageNumber, recommendSize);
		handleItemIdList(newItemList, itemIdSet, recommendSourceMap, null, null, RecommendService.NEW);

		// 商品总榜
		List<Integer> rankingItemList = recommendService.recommendItemByRanking(siteCode, null, null, null, null, pageNumber, recommendSize);
		handleItemIdList(rankingItemList, itemIdSet, recommendSourceMap, null, null, RecommendService.RANKING);

	}

	public void recommendItemListForDetail(Set<Integer> itemIdSet, Map<Integer, String> recommendSourceMap, String siteCode, String deviceId, Integer itemId,
										   String categoryIds, Integer sexFlag, String ageGroupFlag, Integer pageNumber) {
		List<Integer> itemIds = null;
		int recommendSize = 2;
		//	根据个人推荐
		itemIds = recommendService.recommendItemByItem(itemId, 100, siteCode);
		handleItemIdList(itemIds, itemIdSet, recommendSourceMap, pageNumber, recommendSize, RecommendService.MODEL);

		// 商品总榜
		List<Integer> rankingItemList = recommendService.recommendItemByRanking(siteCode, itemId, ageGroupFlag, sexFlag, null, pageNumber, recommendSize);
		handleItemIdList(rankingItemList, itemIdSet, recommendSourceMap, null, null, RecommendService.RANKING);

		// 根据商品排行榜推荐（同分类同年龄段）
		List<Integer> rankingList = recommendService.recommendItemByRanking(siteCode, itemId, ageGroupFlag, sexFlag, categoryIds, pageNumber, recommendSize);
		handleItemIdList(rankingList, itemIdSet, recommendSourceMap, null, null, RecommendService.CATEGORY);

		// 去掉该详情页的产品
		itemIdSet.remove(itemId);
	}

	public void recommendItemListForMine(Set<Integer> itemIdSet, Map<Integer, String> recommendSourceMap, String siteCode, String deviceId, Integer pageNumber) {
		List<Integer> itemIds = null;
		int recommendSize = 2;

		//	根据个人推荐
		itemIds = recommendService.recommendItemByUser(deviceId,  100, siteCode);
		handleItemIdList(itemIds, itemIdSet, recommendSourceMap, pageNumber, recommendSize, RecommendService.MODEL);

		// 商品总榜
		List<Integer> rankingItemList = recommendService.recommendItemByRanking(siteCode, null, null, null, null, pageNumber, recommendSize);
		handleItemIdList(rankingItemList, itemIdSet, recommendSourceMap, null, null, RecommendService.RANKING);
	}

	public void recommendItemListForOrderSuccess(Set<Integer> itemIdSet, Map<Integer, String> recommendSourceMap, String siteCode, String deviceId, String itemIdsStr, Integer pageNumber, Integer pageSize) {

		int recommendSize = 2;
		if (itemIdsStr != null && !itemIdsStr.isEmpty()) {
			String[] itemIdArray = itemIdsStr.split(",");
			for (String itemIdStr : itemIdArray) {
				//	每个itemId推荐
				List<Integer> itemIds = recommendService.recommendItemByUerAndItem(deviceId, Integer.parseInt(itemIdStr), 100, siteCode);
				handleItemIdList(itemIds, itemIdSet, recommendSourceMap, pageNumber, recommendSize, RecommendService.MODEL);
			}
		}
		if (itemIdSet.size() > pageSize) {
			List<Integer> itemIdList = new ArrayList<>(itemIdSet);
			Collections.shuffle(itemIdList);
			itemIdSet.clear();
			itemIdSet.addAll(itemIdList.subList(0, pageSize));
			return;
		}
		// 商品总榜
		List<Integer> rankingItemList = recommendService.recommendItemByRanking(siteCode, null, null, null, null, pageNumber, recommendSize);
		handleItemIdList(rankingItemList, itemIdSet, recommendSourceMap, null, null, RecommendService.RANKING);
	}


	private void handleItemIdList(List<Integer> itemIdList, Set<Integer> itemIdSet, Map<Integer, String> recommendSourceMap, Integer pageNumber, Integer recommendSize, String tag) {
		if (recommendSize != null) {
			if (itemIdList != null && !itemIdList.isEmpty() && itemIdList.size() > (pageNumber - 1) * recommendSize) {
				int startIndex = (pageNumber - 1) * recommendSize;
				int endIndex = Math.min(startIndex + recommendSize, itemIdList.size());
				for (Integer id : itemIdList.subList(startIndex, endIndex)) {
					itemIdSet.add(id);
					if (!recommendSourceMap.containsKey(id)) {
						recommendSourceMap.put(id, tag);
					}
				}
			}
		} else {
			for (Integer id : itemIdList) {
				itemIdSet.add(id);
				// 避免覆盖
				if (!recommendSourceMap.containsKey(id)) {
					recommendSourceMap.put(id, tag);
				}
			}
		}
	}


}
