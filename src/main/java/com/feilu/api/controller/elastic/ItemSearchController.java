package com.feilu.api.controller.elastic;

import com.alibaba.fastjson.JSON;
import com.feilu.api.common.annotation.ValidateEsItemReq;
import com.feilu.api.common.entity.*;
import com.feilu.api.service.elastic.ItemSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dzh
 */
@Slf4j
@RestController
@RequestMapping("/esitem")
public class ItemSearchController{

	private ItemSearchService itemService;

	@Autowired
	public void setItemService(ItemSearchService itemService) {
		this.itemService = itemService;
	}

	/**
	 * 搜索
	 */
	@PostMapping("/search")
	@ValidateEsItemReq
	public ApiResponse<?> search(@RequestBody Map<String,Object> requestParam){
		String requestData = requestParam.get("esItemReq").toString();
		EsItemReq esItemReq = null;
		ApiResponse<?> result = new ApiResponse<>();
		try {
			esItemReq = JSON.parseObject(requestData, EsItemReq.class);
			result = itemService.searchItems(esItemReq);
		} catch (Exception e) {
			log.error("请求数据esItemReq:" + requestData);
            log.error("search异常:{}", e.getMessage(), e);
		}
		return (result);
	}

	/**
	 * 推荐
	 */
	@PostMapping("/recommend")
	@ValidateEsItemReq
	public ApiResponse<?> recommend(@RequestBody Map<String,Object> requestParam){
		String requestData = requestParam.get("esItemReq").toString();
		EsItemReq esItemReq = null;
		ApiResponse<?> result = new ApiResponse<>();
		try {
			esItemReq = JSON.parseObject(requestData, EsItemReq.class);
			result = itemService.recommendItems(esItemReq);
		} catch (Exception e) {
			log.error("请求数据esItemReq:"+ requestData);
            log.error("search异常:{}", e.getMessage(), e);
		}
		return (result);
	}

	/**
	 * 推荐(新)
	 */
	@PostMapping("/recommendNew")
	@ValidateEsItemReq
	public ApiResponse<?> recommendNew(@RequestBody Map<String,Object> requestParam){
		String requestData = requestParam.get("esItemReq").toString();
		EsItemReq esItemReq = null;
		ApiResponse<?> result = new ApiResponse<>();
		try {
			esItemReq = JSON.parseObject(requestData, EsItemReq.class);
			result = itemService.recommendItemsNew(esItemReq);
		} catch (Exception e) {
			log.error("请求数据esItemReq:"+ requestData);
			log.error("search异常:"+ e.getMessage(),e);
		}
		return (result);
	}

	/**
	 * 相似商品
	 */
	@PostMapping("/similar")
	@ValidateEsItemReq
	public ApiResponse<?> similar(@RequestBody Map<String,Object> requestParam){
		String requestData = requestParam.get("esItemReq").toString();
		EsItemReq esItemReq = null;
		ApiResponse<?> result = new ApiResponse<>();
		try {
			esItemReq = JSON.parseObject(requestData, EsItemReq.class);
			result = itemService.querySimilarItems(esItemReq);
		} catch (Exception e) {
			log.error("请求数据esItemReq:"+ requestData);
			log.error("search异常:"+ e.getMessage(),e);
		}
		return (result);
	}

	/**
	 * 获取模型商品
	 */
	@PostMapping("/getModelItems")
	@ValidateEsItemReq
	public ApiResponse<?> getModelItems(@RequestBody Map<String,Object> requestParam){
		String requestData = requestParam.get("esItemReq").toString();
		EsItemReq esItemReq = null;
		ApiResponse<?> result = new ApiResponse<>();
		List<EsItemResp> esItemResps = new ArrayList<>();
		try {
			esItemReq = JSON.parseObject(requestData, EsItemReq.class);
			esItemResps = itemService.queryModelItems(esItemReq);
			result.setState("ok");
			result.setData(esItemResps);
		} catch (Exception e) {
			log.error("请求数据esItemReq:"+ requestData);
            log.error("search异常:{}", e.getMessage(), e);
		}
		return (result);
	}

	/**
	 * 获取聚合字段分类id集合值
	 */
	@PostMapping("/queryCategoryIds")
	@ValidateEsItemReq
	public ApiResponse<?> queryCategoryIds(@RequestBody Map<String,Object> requestParam){
		String requestData = requestParam.get("esItemReq").toString();
		EsItemReq esItemReq = null;
		ApiResponse<?> result = new ApiResponse<>();
		try {
			esItemReq = JSON.parseObject(requestData, EsItemReq.class);
		} catch (Exception e) {
			log.error("请求数据requestData:{}", requestData);
			log.error("queryCategoryIds异常:{}", e.getMessage(), e);
		}
		List<String> list = itemService.queryCategoryIds(esItemReq);
		result.setData(list);
		return result;
	}


	/**
	 * 添加单条数据
     */
	@PostMapping("/save")
	public Ret save(@RequestBody Map<String,Object> requestParam) {
		String siteCode = requestParam.get("siteCode").toString();
		String paramJson= null;
		EsItem esItem = null;
		Ret ret = Ret.create();
		try {
			paramJson = URLDecoder.decode(requestParam.get("itemJsonTxt").toString(), StandardCharsets.UTF_8);
			esItem = JSON.parseObject(paramJson, EsItem.class);
			Integer id = esItem.getItemId();
			ret = itemService.addData(siteCode, esItem.getLanguageCode(), id, paramJson);
		} catch (Exception e) {
            log.error("请求数据paramJson:{}", paramJson);
            log.error("save异常:{}", e.getMessage(), e);
		}
		return ret;
	}

	 /**
	 * 删除单条数据
	 */
	@PostMapping("/delete")
	public Ret delete(@RequestBody Map<String,Object> requestParam){
		String siteCode = requestParam.get("siteCode").toString();
		String languageCode = requestParam.get("languageCode").toString();
		Integer itemId = Integer.parseInt(requestParam.get("itemId").toString());
		Ret ret = Ret.create();
		try {
			ret = itemService.deleteData(siteCode,languageCode,itemId);
		} catch (Exception e) {
            log.error("delete异常:{}", e.getMessage(), e);
		}
		return ret;
	}

	/**
	 * 更新数据
	 */
	@PostMapping("/update")
	public Ret update(@RequestBody Map<String,Object> requestParam){
		String siteCode = requestParam.get("siteCode").toString();
		String languageCode = requestParam.get("languageCode").toString();
		Integer id = Integer.parseInt(requestParam.get("sid").toString());
		String paramJson = null;
		Ret ret = Ret.create();
		try {
			paramJson = URLDecoder.decode(requestParam.get("itemJsonTxt").toString(), StandardCharsets.UTF_8);
			ret = itemService.updateData(siteCode, languageCode, id, paramJson);
		} catch (Exception e) {
			log.error("请求数据paramJson:{}", paramJson);
			log.error("update异常:{}", e.getMessage(), e);
		}
		return ret;
	}

	 /**
	 * ES 批量保存数据（同步）
	 **/
	@PostMapping("/saveDataBatch")
	public Ret saveDataBatch(@RequestBody Map<String,Object> requestParam) {
		// TODO
		String siteCode = requestParam.get("siteCode").toString();
		String languageCode = requestParam.get("languageCode").toString();
		return itemService.saveDataBatch(siteCode, languageCode);
	}

	/**
	 * 更新指定字段数据
     */
	@PostMapping("/updateAppointData")
	public Ret updateAppointData(@RequestBody Map<String,Object> requestParam){
		String siteCode = requestParam.get("siteCode").toString();
		String languageCode = requestParam.get("languageCode").toString();
		Integer id = Integer.parseInt(requestParam.get("id").toString());
		String updateFile = requestParam.get("updateFile").toString();
		String[] updateFiles = null;
		if(updateFile != null){
			updateFiles = updateFile.split(",");
		}
		String updateFilesValue = requestParam.get("updateFilesValue").toString();
		String[] updateFilesValues = null;
		if(updateFilesValue != null){
			updateFilesValues = updateFilesValue.split(",");
		}
		return itemService.updateAppointData(siteCode,languageCode,id,updateFiles,updateFilesValues);
	}

	/**
     * ES 批量修改数据
     **/
	@PostMapping("/updateDataBatch")
	public Ret updateDataBatch(@RequestBody Map<String,Object> requestParam) {
		//TODO
		String siteCode = requestParam.get("siteCode").toString();
		String languageCode = requestParam.get("languageCode").toString();
		return itemService.updateDataBatch(siteCode, languageCode);
	}


	/**
	 * 获取聚合字段kv
	 */
	@PostMapping("/queryAggregation")
	public Map<String, Object> queryAggregation(@RequestBody Map<String,Object> requestParam){
		String name = requestParam.get("keywords").toString();
		String siteCode = requestParam.get("siteCode").toString();
		String languageCode = requestParam.get("languageCode").toString();
		return itemService.queryAggregation(name, siteCode, languageCode);
	}
}
