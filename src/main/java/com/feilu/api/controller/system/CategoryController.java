package com.feilu.api.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.feilu.api.common.entity.Ret;
import com.feilu.api.common.annotation.ValidateRequestData;
import com.feilu.api.service.system.CategoryService;
import com.jfinal.kit.StrKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("sysCategory")
public class CategoryController {

	private CategoryService categoryService;

	@Autowired
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	/**
	 * 查询聚合属性
	 */
	@PostMapping("queryCategoryAttrs")
	@ValidateRequestData
	public Ret queryCategoryAttrs(@RequestBody Map<String,Object> requestParam){

		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		Ret ret = Ret.create();
		String languageCode = map.getString("languageCode");
		Integer categoryId = map.getInteger("categoryId");
		if(StrKit.isBlank(languageCode) || categoryId == null){
			return Ret.fail("msg", "请求参数为空!");
		}
		ret = categoryService.queryCategoryAttrs(languageCode,categoryId);
		return ret;
	}
}
