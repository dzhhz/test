package com.feilu.api.service.elastic.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.SourceFilter;
import co.elastic.clients.json.JsonData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.feilu.api.common.constant.SiteCodeValue;
import com.feilu.api.common.constant.SiteIdConstant;
import com.feilu.api.common.entity.*;
import com.feilu.api.common.kit.FormatKit;
import com.feilu.api.common.utils.EsUtils;
import com.feilu.api.service.elastic.ItemSearchService;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author dzh
 */
@Slf4j
@Service
public class ItemSearchServiceImpl implements ItemSearchService {

	/**
	 * 20条/页  查询全部时最多20页
	 */
	private final static int PAGESIZEMAX = 20;
	/**
	 * {"TW","TH","VN"}
	 */
	private static final String[] SITECODES = {"TW","TH","VN","MY","TW2","TH2","TW3","GR2","PL2","PT2","HU2","CZ2","SG","PH","TWK","TWJ","THJ","VNJ"};
	/**
	 * {"TW9","TH9","VN9"}
	 */
	private static final String[] SITECODES9 = {"TW9","TH9","VN9","MY9","ID9","SG9","TW8","TH8","VN8","MY8","LA8","ID8","SG8","PH8","PH9"};
	//性别词
	private static final String[] SEXKEYS_CN = {"男士","女士","男性","女性","男人","女人","男的","女的","男","女"};
	private static final String[] SEXKEYS_TW = {"男士","女士","男性","女性","男人","女人","男的","女的","男","女"};
	private static final String[] SEXKEYS_TH = {"คุณผู้ชาย","ผู้ชาย","บุรุษ","ชาย","คุณผู้หญิง","สุภาพสตรี","ผู้หญิง","หญิง",};
	private static final String[] SEXKEYS_VN = {"Của nam","Đàn ông","Quý ông","Của nữ","Phụ nữ","Quý cô","Nam","Nữ"};
	private static final String[] SEXKEYS_EN = {"male","man","men","female","woman","women","miss","lady"};
	private static final String[] SEXKEYS_MY = {"lelaki","wanita","perempuan","rindu"};
	private static final String[] SEXKEYS_GR = {"γυναίκες","άνδρες"};
	private static final String[] SEXKEYS_ID = {"pria","wanita","perempuan","rindu"};
	private static final String[] SEXKEYS_LA = {"ບູນນາ","ລົງນາ","ບົວລິກ"};
	//儿童词
	private static final String[] CHILDKEYS_CN = {"男童","男孩","女童","女孩","儿童","婴幼儿","婴儿","幼儿","小孩"};
	private static final String[] CHILDKEYS_TW = {"男童","男孩","女童","女孩","兒童","嬰幼兒","嬰兒","幼兒","小孩"};
	private static final String[] CHILDKEYS_TH = {"เด็กชาย","เด็กผู้ชาย","เด็กหญิง","เด็กผู้หญิง","เด็กเล็ก","ทารก","เด็กๆ","เด็ก",};
	private static final String[] CHILDKEYS_VN = {"bé trai","con trai","bé gái","con gái","trẻ em","trẻ sơ sinh","trẻ nhỏ","em bé","nhi đồng"};
	private static final String[] CHILDKEYS_EN = {"childrens","children","child","kids","kid","baby","girls","girl","boys","boy"};
	private static final String[] CHILDKEYS_MY = {"bayi","anak kecil","kanak"};
	private static final String[] CHILDKEYS_GR = {"aγόρια","kορίτσια","Παιδιά","βρέφη","νήπια"};
	private static final String[] CHILDKEYS_ID = {"anak","bayi"};
	private static final String[] CHILDKEYS_LA = {"ໂຕອັດ","ທູກໂດ","ສູງນາ","ປູກໂດ","ປິກປະກອນ","ອິກປະກອນ","ລາ້ອນດອນ"};

	//冬季月份
	private static final Integer[] MONTH_WINTER = {11,12,1};

	private static final Map<String, String> SITE_CODE_TO_INDEX = new HashMap<>();

	static {
		// 初始化固定的 siteCode 和 indexName 映射
		SITE_CODE_TO_INDEX.put("TH", "flmall_item_th_v2");
		SITE_CODE_TO_INDEX.put("TW", "flmall_item_tw_v2");
		SITE_CODE_TO_INDEX.put("ID", "flmall_item_id_v2");
		SITE_CODE_TO_INDEX.put("PH", "flmall_item_ph_v2");
		SITE_CODE_TO_INDEX.put("TW2", "flmall_item_tw2_v2");
		SITE_CODE_TO_INDEX.put("TW3", "flmall_item_tw3_v2");
		SITE_CODE_TO_INDEX.put("TW4", "flmall_item_tw4_v2");
		SITE_CODE_TO_INDEX.put("JP3", "flmall_item_jp3_v2");
		SITE_CODE_TO_INDEX.put("VN", "flmall_item_vn_v2");
		SITE_CODE_TO_INDEX.put("EN", "flmall_item_en_v1");
		SITE_CODE_TO_INDEX.put("EN2", "flmall_item_en2_v2");
		SITE_CODE_TO_INDEX.put("TH2", "flmall_item_th2_v2");
		SITE_CODE_TO_INDEX.put("TW9", "flmall_item_tw9_v2");
		SITE_CODE_TO_INDEX.put("TH9", "flmall_item_th9_v2");
		SITE_CODE_TO_INDEX.put("VN9", "flmall_item_vn9_v2");
		SITE_CODE_TO_INDEX.put("ID9", "flmall_item_id9_v2");
		SITE_CODE_TO_INDEX.put("TW8", "flmall_item_tw8_v2");
		SITE_CODE_TO_INDEX.put("TH8", "flmall_item_th8_v2");
		SITE_CODE_TO_INDEX.put("VN8", "flmall_item_vn8_v2");
		SITE_CODE_TO_INDEX.put("ID8", "flmall_item_id8_v2");
		SITE_CODE_TO_INDEX.put("LA8", "flmall_item_la8_v2");
		SITE_CODE_TO_INDEX.put("TW7", "flmall_item_tw7_v1");
		SITE_CODE_TO_INDEX.put("TH7", "flmall_item_th7_v1");
		SITE_CODE_TO_INDEX.put("VN7", "flmall_item_vn7_v1");
		SITE_CODE_TO_INDEX.put("JP2", "flmall_item_jp2_v1");
		SITE_CODE_TO_INDEX.put("GR2", "flmall_item_gr2_v2");
		SITE_CODE_TO_INDEX.put("PL2", "flmall_item_pl2_v2");
		SITE_CODE_TO_INDEX.put("PT2", "flmall_item_pt2_v2");
		SITE_CODE_TO_INDEX.put("HU2", "flmall_item_hu2_v2");
		SITE_CODE_TO_INDEX.put("CZ2", "flmall_item_cz2_v2");
		SITE_CODE_TO_INDEX.put("PH8", "flmall_item_ph8_v2");
		SITE_CODE_TO_INDEX.put("PH9", "flmall_item_ph9_v2");
		SITE_CODE_TO_INDEX.put("TWK", "flmall_item_twk_v1");
		SITE_CODE_TO_INDEX.put("TWJ", "flmall_item_twj_v1");
		SITE_CODE_TO_INDEX.put("THJ", "flmall_item_thj_v1");
		SITE_CODE_TO_INDEX.put("VNJ", "flmall_item_vnj_v1");
	}

	private final EsUtils esUtils;

	@Autowired
	public ItemSearchServiceImpl(EsUtils esUtils) {
		this.esUtils = esUtils;
	}

	/**
	 * 搜索
	 */
	@Override
	public ApiResponse<?> searchItems(EsItemReq esItemReq) {
		if(StringUtils.isNotBlank(esItemReq.getKeywords())) {
			log.info(esItemReq.getSiteCode() + " ~ " + esItemReq.getKeywords());
		}
		ApiResponse<?> apiResponse = new ApiResponse<>();

		if(StringUtils.isBlank(esItemReq.getSiteCode())){
			apiResponse.setState("fail");
			apiResponse.setMessage("站点码不能为空!");
			log.error("站点码不能为空");
			return apiResponse;
		}
		BigDecimal minPrice = esItemReq.getMinPrice();
		BigDecimal maxPrice = esItemReq.getMaxPrice();
		if(minPrice.compareTo(BigDecimal.ZERO) > 0 || maxPrice.compareTo(BigDecimal.ZERO) > 0){
			if(minPrice.compareTo(maxPrice) > 0 && maxPrice.compareTo(BigDecimal.ZERO) > 0 ){
				apiResponse.setState("fail");
				apiResponse.setMessage("输入价格区间有误!");
				log.error("输入价格区间有误,最大价格小于最小价格!");
				return apiResponse;
			}
		}

		ElasticsearchClient client = esUtils.getEsClient();

		// 构建 SearchRequest.Builder
		SearchRequest.Builder builder = buildSourceBuilder(esItemReq);
		// 构建queryBuilder
		BoolQuery.Builder queryBuilder = buildSearchQueryBuilder(esItemReq);
		builder.query(q -> q.bool(queryBuilder.build()));
		// 排序
		sortSearch(esItemReq, builder);
		// 索引名称
    	String indexName = getIndexAliasName(esItemReq.getSiteCode(), esItemReq.getLanguageCode());
		SearchRequest searchRequest = builder.index(indexName).build();
		try {
			// 搜索
			SearchResponse<Object> response = client.search(searchRequest, Object.class);
			List<EsItemResp> listJsonData = esUtils.getJsonData(response, esItemReq.getTag(), esItemReq.getKeywords());
			// 填充信息
			fillingData(esItemReq, listJsonData);
			// 调整totalRow 处理listJsonData
			int totalRow = handleResultInfo(response, listJsonData, esItemReq);
			apiResponse = esUtils.getPageData(listJsonData, totalRow, esItemReq.getPageNumber(), esItemReq.getPageSize(),1);
    	} catch (IOException e) {
    		apiResponse = esUtils.getPageData(new ArrayList<>(), 0, esItemReq.getPageNumber(), esItemReq.getPageSize(),0);
    		log.error("索引" + indexName + "查询异常" + e + "请求数据" + searchRequest.source());
    	}
		return apiResponse;
	}

	/**
	 * 推荐
	 */
	@Override
	public ApiResponse<?> recommendItems(EsItemReq esItemReq) {

		ApiResponse<?> apiResponse = new ApiResponse<>();

		if(StringUtils.isBlank(esItemReq.getSiteCode())){
			apiResponse.setState("fail");
			apiResponse.setMessage("站点码不能为空!");
			log.error("站点码不能为空");
			return apiResponse;
		}

		ElasticsearchClient client = esUtils.getEsClient();

		// 构建 SearchRequest.Builder
		SearchRequest.Builder builder = buildSourceBuilder(esItemReq);
		// 构建 BoolQuery.Builder
		BoolQuery.Builder queryBuilder = buildRecommendQueryBuilder(esItemReq);
		builder.query(q -> q.bool(queryBuilder.build()));
		// 排序
		sortRecommend(esItemReq, builder);

		//索引名称
		String indexName = getIndexAliasName(esItemReq.getSiteCode(), esItemReq.getLanguageCode());
		SearchRequest searchRequest = builder.index(indexName).build();

		try {
			SearchResponse<Object> response = client.search(searchRequest, Object.class);
			List<EsItemResp> listJsonData = esUtils.getJsonData(response, esItemReq.getTag(), esItemReq.getKeywords());
			// 填充信息
			fillingData(esItemReq, listJsonData);
			// 调整totalRow 处理listJsonData
			int totalRow = handleResultInfo(response, listJsonData, esItemReq);
			apiResponse = esUtils.getPageData(listJsonData, totalRow, esItemReq.getPageNumber(), esItemReq.getPageSize(),0);

		} catch (IOException e) {
			apiResponse = esUtils.getPageData(new ArrayList<>(), 0, esItemReq.getPageNumber(), esItemReq.getPageSize(),0);
			log.error("索引" + indexName + "查询异常" + e + "请求数据" + searchRequest.source());
		}
		return apiResponse;
	}

	/**
	 * 推荐 new
	 */
	@Override
	public ApiResponse<?> recommendItemsNew(EsItemReq esItemReq) {
		ApiResponse<?> apiResponse = new ApiResponse<>();

		if(StringUtils.isBlank(esItemReq.getSiteCode())){
			apiResponse.setState("fail");
			apiResponse.setMessage("站点码不能为空!");
			log.error("站点码不能为空");
			return apiResponse;
		}
		ElasticsearchClient client = esUtils.getEsClient();

		// 构建 SearchRequest.Builder
		SearchRequest.Builder builder = buildSourceBuilder(esItemReq);
		// 构建 BoolQuery.Builder
		BoolQuery.Builder queryBuilder = buildRecommendQueryBuilderNew(esItemReq);
		builder.query(q -> q.bool(queryBuilder.build()));

		//索引名称
		String indexName = getIndexAliasName(esItemReq.getSiteCode(), esItemReq.getLanguageCode());
		SearchRequest searchRequest = builder.index(indexName).build();

		try {
			SearchResponse<Object> response = client.search(searchRequest, Object.class);
			List<EsItemResp> listJsonData = esUtils.getJsonData(response, esItemReq.getTag(), esItemReq.getKeywords());
			// 填充信息
			fillingData(esItemReq, listJsonData);
			// 调整totalRow 处理listJsonData
			int totalRow = handleResultInfo(response, listJsonData, esItemReq);
			apiResponse = esUtils.getPageData(listJsonData, totalRow, esItemReq.getPageNumber(), esItemReq.getPageSize(),0);

		} catch (IOException e) {
			apiResponse = esUtils.getPageData(new ArrayList<>(), 0, esItemReq.getPageNumber(), esItemReq.getPageSize(),0);
			log.error("索引" + indexName + "查询异常" + e + "请求数据" + searchRequest.source());
		}
		return apiResponse;
	}

	/**
	 * 相似商品
	 */
	@Override
	public ApiResponse<?> querySimilarItems(EsItemReq esItemReq) {
		ApiResponse<?> apiResponse = new ApiResponse<>();

		if(StringUtils.isBlank(esItemReq.getSiteCode())){
			apiResponse.setState("fail");
			apiResponse.setMessage("站点码不能为空!");
			log.error("站点码不能为空");
			return apiResponse;
		}
		ElasticsearchClient client = esUtils.getEsClient();

		// 构建 SearchSourceBuilder
		SearchRequest.Builder builder = buildSourceBuilder(esItemReq);
		// 构建 queryBuilder
		BoolQuery.Builder queryBuilder = buildSimilarQueryBuilder(esItemReq);
		builder.query(q -> q.bool(queryBuilder.build()));
		//排序
		sortSimilar(esItemReq, builder);
		//索引名称
		String indexName = getIndexAliasName(esItemReq.getSiteCode(), esItemReq.getLanguageCode());
		SearchRequest searchRequest = builder.index(indexName).build();

		try {
			SearchResponse<Object> response = client.search(searchRequest, Object.class);
			List<EsItemResp> listJsonData = esUtils.getJsonData(response, esItemReq.getTag(), esItemReq.getKeywords());
			// 填充信息
			fillingData(esItemReq, listJsonData);
			// 调整totalRow 处理listJsonData
			int totalRow = handleResultInfo(response, listJsonData, esItemReq);
			apiResponse = esUtils.getPageData(listJsonData, totalRow, esItemReq.getPageNumber(), esItemReq.getPageSize(),0);

		} catch (IOException e) {
			apiResponse = esUtils.getPageData(new ArrayList<>(), 0, esItemReq.getPageNumber(), esItemReq.getPageSize(),0);
			log.error("索引" + indexName + "查询异常" + e + "请求数据" + searchRequest.source());
		}
		return apiResponse;
	}

	/**
	 * 模型里面的推荐商品
	 */
	@Override
	public List<EsItemResp> queryModelItems(EsItemReq esItemReq) {
		List<EsItemResp> listJsonData = new ArrayList<>();

		ElasticsearchClient client = esUtils.getEsClient();
		// 构建 	SearchSourceBuilder
		SearchRequest.Builder builder = buildSourceBuilder(esItemReq);
		// 构建 BoolQueryBuilder
		BoolQuery.Builder queryBuilder = buildModelQueryBuilder(esItemReq);
		builder.query(f -> f.bool(queryBuilder.build()));
		//排序
		sortRecommend(esItemReq, builder);
		//索引名称
		String indexName = getIndexAliasName(esItemReq.getSiteCode(), esItemReq.getLanguageCode());
		SearchRequest searchRequest = builder.index(indexName).build();

		try {
			SearchResponse<Object> response = client.search(searchRequest, Object.class);
			listJsonData = esUtils.getJsonData(response, esItemReq.getTag(), esItemReq.getKeywords());
			// 填充信息
			fillingData(esItemReq, listJsonData);

		} catch (IOException e) {
			log.error("索引" + indexName + "查询异常" + e + "请求数据" + searchRequest.source());
		}
		return listJsonData;
	}

	/**
	 * 聚合数据返回分类categoryIds
	 */
	@Override
	public List<String> queryCategoryIds(EsItemReq esItemReq) {
		//存放数据
		LinkedList<String> list = new LinkedList<>();

		if (esItemReq == null) {
			log.error("请求参数为空");
			list.add("请求参数为空!");
			return list;
		}
		if(StringUtils.isBlank(esItemReq.getSiteCode())){
			log.error("站点码不能为空");
			list.add("站点码不能为空!");
			return list;
		}

		ElasticsearchClient client = esUtils.getEsClient();
		SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder();
		SourceFilter fetchSource = new SourceFilter.Builder()
				.includes(List.of())
				.excludes(List.of())
				.build();
		searchRequestBuilder.source(f -> f.filter(fetchSource));
		BoolQuery.Builder builder = new BoolQuery.Builder();

		//已上架商品
		builder.must(f -> f.term(t -> t.field("publishStatus").value(9)));
		BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
		//关键字
		if(StringUtils.isNotBlank(esItemReq.getKeywords())) {
			//处理特定的关键词
			handleKeywords(esItemReq, builder, boolQueryBuilder);
		}

		searchRequestBuilder.query(q -> q.bool(builder.build()));
		//索引名称
		String indexName = getIndexAliasName(esItemReq.getSiteCode(), esItemReq.getLanguageCode());
		//聚合字段
		String categoryIds = "categoryIds";
		searchRequestBuilder.aggregations(categoryIds, a -> a.terms(t -> t.field(categoryIds)));
		SearchRequest searchRequest = searchRequestBuilder.index(indexName).build();

		try {
			SearchResponse<Object> response = client.search(searchRequest, Object.class);
			list = esUtils.getAggregationsData(response, categoryIds);

		} catch (IOException e) {
			log.error("索引" + indexName + "查询异常" + e + "请求数据" + searchRequest.source());
		}
		return list;
	}

	/**
	 * 聚合数据返回kv
	 */
	@Override
	public Map<String, Object> queryAggregation(String keywords, String siteCode, String languageCode) {
		Map<String, Object> result = new HashMap<>();

		if(StringUtils.isBlank(siteCode)){
			log.error("站点码siteCode不能为空");
			return result;
		}

		ElasticsearchClient client = esUtils.getEsClient();
		SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder();
		SourceFilter fetchSource = new SourceFilter.Builder()
				.includes(List.of())
				.excludes(List.of())
				.build();
		searchRequestBuilder.source(f -> f.filter(fetchSource));
		BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
		if(StringUtils.isNotBlank(keywords)) {
			boolQueryBuilder.should(f -> f.match(m -> m.field("searchSummary").query(keywords)));
		}
		searchRequestBuilder.query(q -> q.bool(boolQueryBuilder.build()));
		//索引名称
		String indexName = getIndexAliasName(siteCode, languageCode);
		//聚合字段
		String categoryIds = "categoryIds";
		String activityIds = "activityIds";

		searchRequestBuilder.aggregations(categoryIds, a -> a.terms(t -> t.field(categoryIds)));
		searchRequestBuilder.aggregations(activityIds, a -> a.terms(t -> t.field(activityIds)));

		SearchRequest searchRequest = searchRequestBuilder.index(indexName).build();

		try {
			SearchResponse<Object> response = client.search(searchRequest, Object.class);
			result.put(categoryIds, esUtils.getAggregationsData(response, categoryIds));
			result.put(activityIds, esUtils.getAggregationsData(response, activityIds));

		} catch (IOException e) {
			log.error("索引" + indexName + "查询异常" + e + "请求数据" + searchRequest.source());
		}
		return result;
	}

	/**
	 * 添加单条数据
	 */
	@Override
	public Ret addData(String siteCode,String languageCode,Integer id,String paramJson) {
		Ret ret;
		if(siteCode == null || id == null){
			return Ret.fail("msg","参数有误");
		}
		String indexName = getIndexName(siteCode, languageCode);
		String indexId = id.toString();
		boolean result = esUtils.addData(indexName, indexId, paramJson);
		if(result){
			ret = Ret.ok("msg","新增成功");
		}else{
			ret = Ret.fail("msg","新增失败");
		}
		return ret;
	}

	/**
	 * 批量保存数据 todo record
	 */
	@Override
	public Ret saveDataBatch(String siteCode, String languageCode) {
		Ret ret;
		if(siteCode == null){
			return Ret.fail("msg","参数有误");
		}
		String indexName = getIndexName(siteCode, languageCode);
		String primaryKeyName = "id";
		List<Record> testItems = getTestItems(siteCode);
//		for(Record record : testItems){
//			List<Record> itemsAttr = getTestItemsAttr(record.getInt("id"));
//			record.set("attrValuePath", itemsAttr);
//		}
		String paramListJson = JSON.toJSONString(testItems);
		boolean result = esUtils.saveDataBatch(indexName, primaryKeyName, paramListJson);
		if(result){
			ret = Ret.ok("msg","批量新增成功");
		}else{
			ret = Ret.fail("msg","批量新增失败");
		}
		return ret;
	}

	/**
	 * 删除单条数据
	 */
	@Override
	public Ret deleteData(String siteCode,String languageCode,Integer indexId) {
		Ret ret = Ret.create();
		if(siteCode == null || indexId == null){
			return Ret.fail("msg","参数有误");
		}
		String indexName = getIndexName(siteCode, languageCode);
		boolean result = false;

		try {
			String dataById = esUtils.getDataById(indexName, indexId.toString());
			if(dataById == null){
				ret = Ret.ok("msg","操作成功");
				return ret;
			}
			result = esUtils.deleteDate(indexName, indexId.toString());
		} catch (Exception e) {
			ret = Ret.fail("msg","删除异常:"+e);
			return ret;
		}

		if(result){
			ret = Ret.ok("msg","删除成功");
		}else{
			ret = Ret.fail("msg","删除失败");
		}
		return ret;
	}

	/**
	 * 更新数据
	 */
	@Override
	public Ret updateData(String siteCode,String languageCode,Integer id,String paramJson){
		Ret ret = Ret.create();
		if(siteCode == null || id == null){
			return Ret.fail("msg","参数有误");
		}
//		String indexName = getIndexName(siteCode, languageCode);
		String indexName = "flmall_item_tw8_v1";
		String indexId = id.toString();
		boolean result = esUtils.updateData(indexName, indexId, paramJson);
		if(result){
			ret = Ret.ok("msg","更新成功");
		}else{
			ret = Ret.fail("msg","更新失败");
		}
		return ret;
	}

	/**
	 * 更新指定字段数据
	 */
	@Override
	public Ret updateAppointData(String siteCode,String languageCode,Integer id,String[] updateFiles,String[] updateFilesValues){
		Ret ret = Ret.create();
		if(siteCode == null || id == null || updateFiles.length < 1 || updateFilesValues.length < 1){
			return Ret.fail("msg","参数有误");
		}
		if(updateFiles.length != updateFilesValues.length ){
			return Ret.fail("msg","参数不匹配");
		}
		String indexName = getIndexName(siteCode, languageCode);
		String indexId = id.toString();
		Map<Object, Object> kv = new HashMap();
		for(int i=0; i<updateFiles.length; i++){
			kv.put(updateFiles[i], updateFilesValues[i]);
		}
		String paramJson = JSON.toJSONString(kv);
		boolean result = esUtils.updateAppointData(indexName, indexId, paramJson,updateFiles);
		if(result){
			ret = Ret.ok("msg","更新指定字段成功");
		}else{
			ret = Ret.fail("msg","更新指定字段失败");
		}
		return ret;
	}

	/**
	 * 批量修改数据
	 */
	@Override
	public Ret updateDataBatch(String siteCode, String languageCode) {
		Ret ret = Ret.create();
		if(siteCode == null){
			return Ret.fail("msg","参数有误");
		}
		String indexName = getIndexName(siteCode, languageCode);
		String primaryKeyName = "id";
		List<Record> testItems = getTestItems(siteCode);
		String paramListJson = JSON.toJSONString(testItems);
		boolean result = esUtils.updateDataBatch(indexName, primaryKeyName, paramListJson);
		if(result){
			ret = Ret.ok("msg","批量更新成功");
		}else{
			ret = Ret.fail("msg","批量更新失败");
		}
		return ret;
	}

	/**
	 * 构建 SearchSourceBuilder
	 */
	private SearchRequest.Builder buildSourceBuilder(EsItemReq esItemReq) {
		int from = (esItemReq.getPageNumber() - 1) * esItemReq.getPageSize();
		int size = esItemReq.getPageSize();
		SourceFilter fetchSource = new SourceFilter.Builder()
				.includes(List.of(
						"title", "marketPrice", "salePrice", "marketPriceTxt", "salePriceTxt",
						"discountTxt", "picUrl", "currencycode", "sid", "itemId", "skuImageList",
						"promoName", "promoType", "workId", "priceType"
				))
				.excludes(List.of())
				.build();
        return new SearchRequest.Builder()
				.from(from)
				.size(size)
				.source(f -> f.filter(fetchSource));
	}

	/**
	 * 构建搜索 queryBuilder
	 */
	private BoolQuery.Builder buildSearchQueryBuilder(EsItemReq esItem) {
		BoolQuery.Builder builder = new BoolQuery.Builder();
		// 已上架商品
		builder.must(f -> f.term(t -> t.field("publishStatus").value(9)));

		BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
		//处理关键词
		handleKeywords(esItem, builder, boolQueryBuilder);
		// 简介
		if(StringUtils.isNotBlank(esItem.getSummary())){
			boolQueryBuilder.should(f -> f.match(m -> m.field("summary").query(esItem.getSummary()).boost(2.0f)));
		}
		builder.must(f -> f.bool(boolQueryBuilder.build()));

		addFilterTerms(builder, esItem);
		addRangeQueries(builder, esItem);
		addNestedQueries(builder, esItem);
		addSeasonFilters(builder, esItem);
		addPromoTypeFilter(builder, esItem);
		addTimeFilters(builder, esItem);
		addMatchQuery(builder, esItem);
		return builder;
	}

	/**
	 * 构建推荐 queryBuilder
	 */
	private BoolQuery.Builder buildRecommendQueryBuilder(EsItemReq esItemReq) {
		BoolQuery.Builder builder = new BoolQuery.Builder();

		//已上架商品
		builder.must(f -> f.term(t -> t.field("publishStatus").value(9)));

		addMatchQuery(builder, esItemReq);
		addPromoTypeFilter(builder, esItemReq);
		addSeasonFilters(builder, esItemReq);
		addFilterTerms(builder, esItemReq);

		//去除当前商品
		addRemoveCurrentItem(builder, esItemReq);
		//去除已存在的商品
		addRemoveExistedItems(builder, esItemReq);
		return builder;
	}

	/**
	 * 构建推荐 queryBuilder new
	 */
	private BoolQuery.Builder buildRecommendQueryBuilderNew(EsItemReq esItemReq) {

		BoolQuery.Builder builder = new BoolQuery.Builder();

		//已上架商品
		builder.must(f -> f.term(t -> t.field("publishStatus").value(9)));

		//类目搜索  ES的数据格式是空格存储
		if(esItemReq.getCategoryIds() != null){
			builder.must(f -> f.match(m -> m.field("categoryIds").query(esItemReq.getCategoryIds().replace(",", " "))));
		}

		//搜索需要的商品
		addNeedItems(builder, esItemReq);
		//去除当前商品
		addRemoveCurrentItem(builder, esItemReq);
		//去除已存在的商品
		addRemoveExistedItems(builder, esItemReq);
		addPromoTypeFilter(builder, esItemReq);

		return builder;
	}

	/**
	 * 构建相似 queryBuilder
	 */
	private BoolQuery.Builder buildSimilarQueryBuilder(EsItemReq esItemReq) {

		BoolQuery.Builder builder = new BoolQuery.Builder();

		//已上架商品
		builder.must(f -> f.term(t -> t.field("publishStatus").value(9)));
		//类目搜索  ES的数据格式是空格存储
		if(esItemReq.getCategoryIds() != null){
			builder.must(f -> f.match(m -> m.field("categoryIds").query(esItemReq.getCategoryIds().replace(",", " "))));
		}
		addRemoveCurrentItem(builder, esItemReq);
		addPromoTypeFilter(builder, esItemReq);
		return builder;
	}

	/**
	 * 构建模型 queryBuilder
	 */
	private BoolQuery.Builder buildModelQueryBuilder(EsItemReq esItemReq) {
		BoolQuery.Builder builder = new BoolQuery.Builder();

		//已上架商品
		builder.must(f -> f.term(t -> t.field("publishStatus").value(9)));
		addRemoveCurrentItem(builder, esItemReq);

		//类目搜索	ES的数据格式是空格存储
		if(esItemReq.getCategoryIds() != null){
			builder.must(f -> f.match(m -> m.field("categoryIds").query(esItemReq.getCategoryIds().replace(",", " "))));
		}

		addFilterTerms(builder, esItemReq);
		addPromoTypeFilter(builder, esItemReq);
		return builder;
	}

	/**
	 * 添加筛选条件
	 */
	private void addFilterTerms(BoolQuery.Builder builder, EsItemReq esItem) {
		// 性别  1:女  2:男  3:通用
		if (esItem.getSexFlag() != null) {
			builder.must(f -> f.term(t -> t.field("sexFlag").value(esItem.getSexFlag())));
		}
		// 是否大码
		if (esItem.getPlusSizeFlag() != null) {
			builder.must(f -> f.term(t -> t.field("plusSizeFlag").value(esItem.getPlusSizeFlag())));
		}
		// 是否老年装
		if (esItem.getOldAgeFlag() != null) {
			builder.must(f -> f.term(t -> t.field("oldAgeFlag").value(esItem.getOldAgeFlag())));
		}
		// 1:引流 2:特价 3:正价 4:精品
		if (esItem.getPriceType() != null) {
			builder.must(f -> f.term(t -> t.field("priceType").value(esItem.getPriceType())));
		}
	}

	/**
	 * 添加价格范围查询
	 */
	private void addRangeQueries(BoolQuery.Builder builder, EsItemReq esItem) {
		BigDecimal minPrice = esItem.getMinPrice();
		BigDecimal maxPrice = esItem.getMaxPrice();
		RangeQuery.Builder rangeQueryBuilder = new RangeQuery.Builder();
		if (minPrice != null) {
			rangeQueryBuilder.field("salePrice").gte(JsonData.of(minPrice));
		}
		if (maxPrice != null) {
			rangeQueryBuilder.field("salePrice").lte(JsonData.of(maxPrice));
		}
		builder.must(f -> f.range(rangeQueryBuilder.build()));
	}

	/**
	 * 添加 Nested 查询
	 */
	private void addNestedQueries(BoolQuery.Builder builder, EsItemReq esItem) {
		if (esItem.getAttrsList() != null) {
			JSONArray attrsList = JSON.parseArray(esItem.getAttrsList());
            for (Object o : attrsList) {
                EsItemAttr esItemAttr = JSON.parseObject(o.toString(), EsItemAttr.class);
                if (StringUtils.isNotBlank(esItemAttr.getAttrKey())) {
                    NestedQuery nestedQuery = new NestedQuery.Builder()
                            .path("attrsList")
                            .query(q -> q.term(t -> t
                                            .field("attrsList.attrKey")
                                            .value(esItemAttr.getAttrKey())
                                    )
                            )
                            .scoreMode(ChildScoreMode.Sum).build();
                    builder.must(f -> f.nested(nestedQuery));
                }
                if (StringUtils.isNotBlank(esItemAttr.getAttrValue())) {
                    NestedQuery nestedQuery = new NestedQuery.Builder()
                            .path("attrsList")
                            .query(q -> q.term(t -> t
                                            .field("attrsList.attrValue")
                                            .value(esItemAttr.getAttrKey().replace(",", " "))
                                    )
                            )
                            .scoreMode(ChildScoreMode.Sum).build();
                    builder.must(f -> f.nested(nestedQuery));
                }
            }
		}
	}

	/**
	 * 添加季节过滤条件
	 */
	private void addSeasonFilters(BoolQuery.Builder builder, EsItemReq esItem) {
		// pdd 季节 	ES的数据格式是空格存储
		// 1:春   2:夏   3:秋  4:冬 5通用  逗号隔开
		int month = DateUtil.month(new Date()) + 1;

		if (esItem.getSeasonFlag() != null) {
			builder.must(f -> f.match(t -> t.field("seasonFlag").query(esItem.getSeasonFlag().replace(",", " "))));
		} else if (ArrayUtil.contains(SITECODES9, esItem.getSiteCode())) {
			handleSeasonBoost(builder, esItem, month);
		}
	}

	/**
	 * 添加促销类型过滤条件
	 */
	private void addPromoTypeFilter(BoolQuery.Builder builder, EsItemReq esItem) {
		// 默认不查询特殊活动   promoType=200:顺手买一件   promoType=300:免费商品
		if (esItem.getPromoType() == null) {
			builder.mustNot(f -> f.term( t -> t.field("promoType").value(200)));
			builder.mustNot(f -> f.term( t -> t.field("promoType").value(300)));
		} else {
			builder.mustNot(f -> f.term( t -> t.field("promoType").value(esItem.getPromoType())));
		}
	}

	/**
	 * 添加matchQuery
	 */
	private void addMatchQuery(BoolQuery.Builder builder, EsItemReq esItemReq) {
		// 如果是儿童站且年龄组不空
		if(esItemReq.getAgeGroupFlag() != null){
			if(esItemReq.getSiteCode()!=null && esItemReq.getSiteCode().endsWith("K")) {
				MatchQuery matchQuery = new MatchQuery.Builder().field("ageGroupFlag").query(esItemReq.getAgeGroupFlag().replace(",", " ")).build();
				builder.must(f -> f.match(matchQuery));
			}
		}

		// 类目搜索  ES的数据格式是空格存储
		if(esItemReq.getCategoryIds() != null){
			MatchQuery matchQuery = new MatchQuery.Builder().field("categoryIds").query(esItemReq.getCategoryIds().replace(",", " ")).build();
			builder.must(f -> f.match(matchQuery));
		}

		// 活动搜索  ES的数据格式是空格存储
		if(esItemReq.getActivityIds() != null){
			MatchQuery matchQuery = new MatchQuery.Builder().field("activityIds").query(esItemReq.getActivityIds().replace(",", " ")).build();
			builder.must(f -> f.match(matchQuery));
		}
	}

	/**
	 * 添加时间过滤条件
	 */
	private void addTimeFilters(BoolQuery.Builder builder, EsItemReq esItemReq) {
		//开始时间
		if(esItemReq.getBeginTime() != null){
			RangeQuery rangeQuery = new RangeQuery.Builder().field("gmtCreate").gte(JsonData.of(esItemReq.getBeginTime())).build();
			builder.must(f -> f.range(rangeQuery));
		}
		//结束时间
		if(esItemReq.getEndTime() != null){
			RangeQuery rangeQuery = new RangeQuery.Builder().field("gmtCreate").lte(JsonData.of(esItemReq.getEndTime())).build();
			builder.must(f -> f.range(rangeQuery));
		}
	}

	/**
	 * 去除当前商品
	 */
	private void addRemoveCurrentItem(BoolQuery.Builder builder, EsItemReq esItemReq) {
		if(StringUtils.isNotBlank(esItemReq.getSid())){
			TermQuery termQuery = new TermQuery.Builder().field("sid").value(esItemReq.getSid()).build();
			builder.mustNot(f -> f.term(termQuery));
		}
	}

	/**
	 * 去除已存在的商品
	 */
	private void addRemoveExistedItems(BoolQuery.Builder builder, EsItemReq esItemReq) {
		List<Integer> existedItemIdList = esItemReq.getExistedItemIdList();
		if (existedItemIdList != null && !existedItemIdList.isEmpty()) {
			List<FieldValue> fieldValues = existedItemIdList.stream().map(FieldValue::of).toList();
			TermsQuery termsQuery = new TermsQuery.Builder().field("itemId").terms(v -> v.value(fieldValues)).build();
			BoolQuery boolQuery = new BoolQuery.Builder().mustNot(f -> f.terms(termsQuery)).build();
			builder.must(f -> f.bool(boolQuery));
		}
	}

	/**
	 * 添加需要的商品
	 */
	private void addNeedItems(BoolQuery.Builder builder, EsItemReq esItemReq) {
		List<Integer> itemIdList = esItemReq.getItemIdList();
		if (itemIdList != null && !itemIdList.isEmpty()) {
			List<FieldValue> fieldValues = itemIdList.stream().map(FieldValue::of).toList();
			TermsQuery termsQuery = new TermsQuery.Builder().field("itemId").terms(v -> v.value(fieldValues)).boost(2.0f).build();
			BoolQuery boolQuery = new BoolQuery.Builder().should(f -> f.terms(termsQuery)).build();
			builder.must(f -> f.bool(boolQuery));
		}

	}

	/**
	 * 处理季节
	 */
	private void handleSeasonBoost(BoolQuery.Builder builder, EsItemReq esItemReq, int month) {
		// 夏季排在最前边
		float boost = 10.0f;
		if ("TW9".equals(esItemReq.getSiteCode()) || "TW8".equals(esItemReq.getSiteCode())) {
			if (ArrayUtil.contains(MONTH_WINTER, month)) {
				boost = 1.0f;
			}
		}
		MatchQuery summerMatchQuery = new MatchQuery.Builder().field("seasonFlag").query("2").boost(boost).build();
		builder.should(q -> q.match(summerMatchQuery));
		// 通用
		builder.should(q -> q.match(new MatchQuery.Builder().field("seasonFlag").query("5").boost(8.0f).build()));
		// 春秋
		builder.should(q -> q.match(new MatchQuery.Builder().field("seasonFlag").query("1 3").boost(5.0f).build()));
		// 默认值
		builder.should(q -> q.match(new MatchQuery.Builder().field("seasonFlag").query("0").boost(2.0f).build()));
		// 冬季
		if ("TW9".equals(esItemReq.getSiteCode()) || "TW8".equals(esItemReq.getSiteCode())) {
			if (ArrayUtil.contains(MONTH_WINTER, month)) {
				boost = 10.0f;
			}
		}
		MatchQuery winterMatchQuery = new MatchQuery.Builder().field("seasonFlag").query("4").boost(boost).build();
		builder.should(q -> q.match(winterMatchQuery));
	}


	/**
	 * 搜索排序
	 */
	private void sortSearch(EsItemReq esItemReq, SearchRequest.Builder builder) {
		if(esItemReq.getSort() == 1){
			builder.sort(f -> f.field(o -> o.field("salePrice").order(SortOrder.Asc)));
		}else if(esItemReq.getSort() == 2){
			builder.sort(f -> f.field(o -> o.field("salePrice").order(SortOrder.Desc)));
		}else if(esItemReq.getBeginTime() != null || esItemReq.getEndTime() != null){
			builder.sort(f -> f.field(o -> o.field("gmtCreate").order(SortOrder.Desc)));
		}else if(esItemReq.getNewFlag() != null){
			if (StringUtils.isNotBlank(esItemReq.getKeywords())) {
				builder.sort(f -> f.field(o -> o.field("_score").order(SortOrder.Desc)));
			}
			builder.sort(f -> f.field(o -> o.field("gmtCreate").order(SortOrder.Desc)));
		}else {
			//提高得分倍数boost之后,根据评分进行排序
			builder.sort(f -> f.field(o -> o.field("_score").order(SortOrder.Desc)));
			if(StringUtils.isBlank(esItemReq.getKeywords()) && StringUtils.isBlank(esItemReq.getSummary())) {
				Map<String, JsonData> params = new HashMap<>();
				params.put("randomSort", JsonData.of(DateUtil.today() + " 00:00:00"));
				ScriptSort scriptSort = new ScriptSort.Builder()
						.script(s -> s.inline(i -> i.params(params)
								.source("(doc['itemId'].value + params['randomSort']).hashCode()")))
						.type(ScriptSortType.Number)
						.order(SortOrder.Desc)
						.build();
				builder.sort(SortOptions.of(s -> s.script(scriptSort)));
			}
		}
	}

	/**
	 * 推荐排序
	 */
	private static void sortRecommend(EsItemReq esItemReq, SearchRequest.Builder builder) {
		//排序  新款 newFlag=1 推荐 recommendFlag=1   热门 hotFlag=1
		if(esItemReq.getSort()== 1){
			builder.sort(f -> f.field(o -> o.field("salePrice").order(SortOrder.Asc)));
		}else if(esItemReq.getSort() == 2){
			builder.sort(f -> f.field(o -> o.field("salePrice").order(SortOrder.Desc)));
		}else if(esItemReq.getNewFlag() != null){
			builder.sort(f -> f.field(o -> o.field("gmtCreate").order(SortOrder.Desc)));
		}else if(esItemReq.getRecommendFlag() != null){
			builder.sort(f -> f.field(o -> o.field("recommendFlag").order(SortOrder.Desc)));
		}else if(esItemReq.getHotFlag() != null){
			builder.sort(f -> f.field(o -> o.field("hotFlag").order(SortOrder.Desc)));
		}
		else {
			//提高得分倍数boost之后,根据评分进行排序
			builder.sort(f -> f.field(o -> o.field("_score").order(SortOrder.Desc)));
			if(StringUtils.isBlank(esItemReq.getKeywords())) {
				Map<String, JsonData> params = new HashMap<>();
				if(StringUtils.isBlank(esItemReq.getSid())) {
					params.put("randomSort", JsonData.of(DateUtil.today() + " 00:00:00"));
				}
				else {
					params.put("randomSort", JsonData.of(esItemReq.getSid() + DateUtil.today() + " 00:00:00"));
				}
				ScriptSort scriptSort = new ScriptSort.Builder()
						.script(s -> s.inline(i -> i.params(params)
								.source("(doc['itemId'].value + params['randomSort']).hashCode()")))
						.type(ScriptSortType.Number)
						.order(SortOrder.Desc)
						.build();
				builder.sort(SortOptions.of(s -> s.script(scriptSort)));
			}
		}
	}

	/**
	 * 相似排序
	 */
	private static void sortSimilar(EsItemReq esItemReq, SearchRequest.Builder builder) {
		//排序
		if(StringUtils.isBlank(esItemReq.getKeywords())) {
			Map<String, JsonData> params = new HashMap<>();
			if(StringUtils.isBlank(esItemReq.getSid())) {
				params.put("randomSort", JsonData.of(DateUtil.today() + " 00:00:00"));
			}
			else {
				params.put("randomSort", JsonData.of(esItemReq.getSid() + DateUtil.today() + " 00:00:00"));
			}
			ScriptSort scriptSort = new ScriptSort.Builder()
					.script(s -> s.inline(i -> i.params(params)
							.source("(doc['itemId'].value + params['randomSort']).hashCode()")))
					.type(ScriptSortType.Number)
					.order(SortOrder.Desc)
					.build();
			builder.sort(SortOptions.of(s -> s.script(scriptSort)));
		}
	}

	/**
	 * 调整totalRow 处理listJsonData
	 */
	private int handleResultInfo(SearchResponse<Object> response, List<EsItemResp> listJsonData, EsItemReq esItemReq) {
		int totalRow = EsUtils.getDataNum(response);
		if (esItemReq.getCategoryIds() == null || "1".equals(esItemReq.getCategoryIds()) || "0".equals(esItemReq.getCategoryIds())) {
			if(StringUtils.isBlank(esItemReq.getKeywords())){
				//20条/页 查询全部时最多20页
				if(totalRow > PAGESIZEMAX * esItemReq.getPageSize()) {
					totalRow = PAGESIZEMAX * esItemReq.getPageSize();
					if (esItemReq.getPageNumber() * esItemReq.getPageSize() > totalRow) {
						listJsonData.clear();
					}
				}
			}
		}
		return totalRow;
	}

	/**
	 * 填充信息
	 */
	private void fillingData(EsItemReq esItemReq, List<EsItemResp> listJsonData) {
		if (listJsonData == null || listJsonData.isEmpty()) {
			return;
		}
		//putshe
		if(ArrayUtil.contains(SiteIdConstant.SITEID, esItemReq.getSiteId())) {
            for (EsItemResp esItemResp : listJsonData) {
                if (esItemResp.getPriceType() == 2) {//特价
                    esItemResp.setMarketPrice(FormatKit.getPrice(esItemReq.getSiteCode(), esItemReq.getCurrency(), new BigDecimal(esItemResp.getMarketPrice()).add(SiteIdConstant.addPrice)).toString());
                    esItemResp.setSalePrice(FormatKit.getPrice(esItemReq.getSiteCode(), esItemReq.getCurrency(), new BigDecimal(esItemResp.getSalePrice()).add(SiteIdConstant.addPrice)).toString());
                    esItemResp.setMarketPriceTxt(FormatKit.getCurrencyPriceTxt(esItemReq.getCurrency(), new BigDecimal(esItemResp.getMarketPrice())));
                    esItemResp.setSalePriceTxt(FormatKit.getCurrencyPriceTxt(esItemReq.getCurrency(), new BigDecimal(esItemResp.getSalePrice())));

                    esItemResp.setDiscountTxt(FormatKit.getPercentageTxt(new BigDecimal(esItemResp.getMarketPrice()).subtract(new BigDecimal(esItemResp.getSalePrice())).divide(new BigDecimal(esItemResp.getMarketPrice()), 2, RoundingMode.HALF_EVEN)));
                }
            }
		}
		if(StringUtils.isNoneBlank(esItemReq.getCurrency()) && !"NULL".equals(esItemReq.getCurrency())){
			String currencyCode = FormatKit.getSiteBySiteCode(esItemReq.getSiteCode()).getCurrencyCode();
			for(EsItemResp esItemResp : listJsonData){
				if(!currencyCode.equals(esItemReq.getCurrency())){
					esItemResp.setMarketPrice(FormatKit.getPrice(esItemReq.getSiteCode(), esItemReq.getCurrency(), new BigDecimal(esItemResp.getMarketPrice())).toString());
					esItemResp.setSalePrice(FormatKit.getPrice(esItemReq.getSiteCode(), esItemReq.getCurrency(), new BigDecimal(esItemResp.getSalePrice())).toString());
					esItemResp.setMarketPriceTxt(FormatKit.getCurrencyPriceTxt(esItemReq.getCurrency(), new BigDecimal(esItemResp.getMarketPrice())));
					esItemResp.setSalePriceTxt(FormatKit.getCurrencyPriceTxt(esItemReq.getCurrency(), new BigDecimal(esItemResp.getSalePrice())));

				}
				esItemResp.setUsdPrice(FormatKit.getUsdPrice(esItemReq.getSiteCode(), currencyCode, new BigDecimal(esItemResp.getSalePrice())));
			}
		}
	}

	/**
	 * 处理keywords
	 */
	private void handleKeywords(EsItemReq esItemReq, BoolQuery.Builder builder, BoolQuery.Builder builder2) {
		if (StringUtils.isBlank(esItemReq.getKeywords())) {
			return;
		}
		String keywords = esItemReq.getKeywords();
		String siteCode = esItemReq.getSiteCode();
		String languageCode = esItemReq.getLanguageCode();
		String pattern = "^[0-9]*$";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(keywords);

		if (m.matches()) {
			// 处理数字匹配 itemId
			keywords = m.group(0);
			MatchQuery matchQuery = new MatchQuery.Builder()
					.field("itemId")
					.query(keywords)
					.boost(2.0f)
					.build();
			builder2.should(q -> q.match(matchQuery));
			return;
		}

		// 处理非数字关键词
		keywords = processSiteSpecificKeywords(siteCode, keywords);

		// 处理性别
		int sex = determineSex(keywords);
		if (ArrayUtil.contains(SITECODES9, siteCode) && sex != 0) {
			esItemReq.setSexFlag(sex);
		}

		// 先判断是否是儿童
		Map<String, Object> childKeywordMap = isChildKeyword(siteCode, languageCode, keywords);
		// 根据是否是儿童处理分类id
		if (Boolean.parseBoolean(childKeywordMap.getOrDefault("isChild", "false").toString())) {
			keywords = childKeywordMap.getOrDefault("keywords", keywords).toString();
			handleChildCategory(builder, siteCode);
		} else {
			keywords = removeSexKeywords(siteCode,languageCode, keywords, sex);
			handleAdultCategory(builder, siteCode, sex);
		}

		// 处理标题和摘要的匹配
		if (StringUtils.isNotBlank(keywords.trim())) {
			MatchQuery matchQueryTitle = new MatchQuery.Builder().field("title").query(keywords).boost(2.0f).build();
			MatchQuery matchQuerySummary = new MatchQuery.Builder().field("summary").query(keywords).build();
			builder2.should(q -> q.match(matchQueryTitle));
			builder2.should(q -> q.match(matchQuerySummary));
		}
	}

	/**
	 * 繁转简
	 */
	private String processSiteSpecificKeywords(String siteCode, String keywords) {
		if (siteCode.startsWith(SiteCodeValue.TYPE_TW)) {
			keywords = ZhConverterUtil.toTraditional(keywords);
		}
		return keywords;
	}

	/**
	 * 决定性别
	 */
	private int determineSex(String keywords) {
		Set<String> maleKeywords = Stream.of("男", "nam", "nữ", "male", "man", "men", "ชาย", "lelaki")
                .map(String::toLowerCase).collect(Collectors.toSet());

		Set<String> femaleKeywords = Stream.of("女", "female", "women", "woman", "lady", "miss", "หญิง", "perempuan")
                .map(String::toLowerCase).collect(Collectors.toSet());
		String lowerKeywords = keywords.toLowerCase();
		if (maleKeywords.stream().anyMatch(keyword -> lowerKeywords.contains(keyword.toLowerCase()))) {
            return 2;
        }
		if (femaleKeywords.stream().anyMatch(keyword -> lowerKeywords.contains(keyword.toLowerCase()))) {
            return 1;
        }
		return 0;
	}

	/**
	 * 判断是否是儿童
	 */
	private Map<String, Object> isChildKeyword(String siteCode, String languageCode, String keywords) {
		Map<String, Object> result;
		if (siteCode.startsWith(SiteCodeValue.TYPE_TW)) {
			result = handleChildKeywords(keywords, CHILDKEYS_TW);
		} else if (siteCode.startsWith(SiteCodeValue.TYPE_TH)) {
			result = handleChildKeywords(keywords, CHILDKEYS_TH);
		} else if (siteCode.startsWith(SiteCodeValue.TYPE_VN)) {
			result = handleChildKeywords(keywords, CHILDKEYS_VN);
		} else if (siteCode.startsWith(SiteCodeValue.TYPE_ID)) {
			result = handleChildKeywords(keywords, CHILDKEYS_ID);
		} else if (siteCode.startsWith(SiteCodeValue.TYPE_GR)) {
			result = handleChildKeywords(keywords, CHILDKEYS_GR);
		} else if (siteCode.startsWith(SiteCodeValue.TYPE_LA)) {
			result = handleChildKeywords(keywords, CHILDKEYS_LA);
		} else if (siteCode.startsWith(SiteCodeValue.TYPE_MY)) {
			if ("zh-CN".equals(languageCode)) {
				result = handleChildKeywords(keywords, CHILDKEYS_CN);
			} else if ("ms-MY".equals(languageCode)) {
				result = handleChildKeywords(keywords, CHILDKEYS_MY);
			} else {
				result = handleChildKeywords(keywords, CHILDKEYS_EN);
			}
		} else if (siteCode.startsWith(SiteCodeValue.TYPE_SG)) {
			if ("zh-CN".equals(languageCode)) {
				result = handleChildKeywords(keywords, CHILDKEYS_CN);
			} else {
				result = handleChildKeywords(keywords, CHILDKEYS_EN);
			}
		} else {
			result = handleChildKeywords(keywords, CHILDKEYS_EN);
		}
		return result;
	}

	/**
	 * 移除性别关键词
	 */
	private String removeSexKeywords(String siteCode, String languageCode, String keywords, int sex) {
		if (sex != 0) {
			if(siteCode.startsWith(SiteCodeValue.TYPE_TW)){
				keywords = handleSexKeywords(keywords, SEXKEYS_TW);
			}else if (siteCode.startsWith(SiteCodeValue.TYPE_TH)) {
				keywords = handleSexKeywords(keywords, SEXKEYS_TH);
			}else if (siteCode.startsWith(SiteCodeValue.TYPE_VN)) {
				keywords = handleSexKeywords(keywords, SEXKEYS_VN);
			}else if (siteCode.startsWith(SiteCodeValue.TYPE_GR)) {
				keywords = handleSexKeywords(keywords, SEXKEYS_GR);
			}else if (siteCode.startsWith(SiteCodeValue.TYPE_ID)) {
				keywords = handleSexKeywords(keywords, SEXKEYS_ID);
			}else if (siteCode.startsWith(SiteCodeValue.TYPE_LA)) {
				keywords = handleSexKeywords(keywords, SEXKEYS_LA);
			}else if (siteCode.startsWith(SiteCodeValue.TYPE_MY)) {
				if ("zh-CN".equals(languageCode)) {
					keywords = handleSexKeywords(keywords, SEXKEYS_CN);
				} else if ("ms-MY".equals(languageCode)) {
					keywords = handleSexKeywords(keywords, SEXKEYS_MY);
				}else {
					keywords = handleSexKeywords(keywords, SEXKEYS_EN);
				}
			}else if (siteCode.startsWith(SiteCodeValue.TYPE_SG)) {
				if ("zh-CN".equals(languageCode)) {
					keywords = handleSexKeywords(keywords, SEXKEYS_CN);
				} else {
					keywords = handleSexKeywords(keywords, SEXKEYS_EN);
				}
			}else {
				keywords = handleSexKeywords(keywords, SEXKEYS_EN);
			}
	  }
		return keywords;
	}

	/**
	 * 处理儿童关键词
	 */
	private Map<String, Object> handleChildKeywords(String keywords, String[] childKeys) {
		boolean isChild = false;
		for (String sexStr : childKeys) {
			if (keywords.toLowerCase().contains(sexStr.toLowerCase())) {
				keywords = keywords.toLowerCase().replace(sexStr.toLowerCase(), " ");
				isChild = true;
				break;
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("isChild", isChild);
		result.put("keywords", keywords);
		return result;
	}

	/**
	 * 处理性别关键词
	 */
	private String handleSexKeywords(String keywords, String[] sexKeys) {
		for (String sexStr : sexKeys) {
			keywords = keywords.toLowerCase().replace(sexStr, " ");
		}
		return keywords;
	}

	/**
	 * 处理儿童分类
	 */
	private void handleChildCategory(BoolQuery.Builder builder, String siteCode) {
		String categoryIds = ArrayUtil.contains(SITECODES9, siteCode) ? "14933 14966" : "302";
		MatchQuery matchQuery = new MatchQuery.Builder()
				.field("categoryIds")
				.query(categoryIds)
				.build();
		builder.must(Query.of(q -> q.match(matchQuery)));
	}

	/**
	 * 处理成人分类
	 */
	private void handleAdultCategory(BoolQuery.Builder builder, String siteCode, int sex) {
		if (sex != 0) {
			// 去除儿童
			String categoryIds = ArrayUtil.contains(SITECODES9, siteCode) ? "14933 14966" : "302";
			MatchQuery matchQueryNot = new MatchQuery.Builder()
					.field("categoryIds")
					.query(categoryIds)
					.build();
			builder.mustNot(Query.of(q -> q.match(matchQueryNot)));
			// 非pop站查询女/男 装/鞋
			if (!ArrayUtil.contains(SITECODES9, siteCode)) {
				MatchQuery matchQuery = new MatchQuery.Builder()
						.field("categoryIds")
						.query(sex == 2 ? "3 62 86" : "2 61 85")
						.build();
				builder.must(Query.of(q -> q.match(matchQuery)));
			}
		}
	}

	/**
	 * 查询 别名方式
	 */
	private String getIndexAliasName(String siteCode, String languageCode){
		//索引别名
		if (siteCode.startsWith("MY")) {
			if(StringUtils.isBlank(languageCode)) {
				return "flmall_item_"+siteCode.toLowerCase();
			}
			if ("en-us".equalsIgnoreCase(languageCode)) {
				return "flmall_item_"+siteCode.toLowerCase();
			} else {
				return "flmall_item_"+siteCode.toLowerCase()+"_"+languageCode.toLowerCase();
			}
		}
		else if (siteCode.startsWith("SG")) {
			if(StringUtils.isBlank(languageCode)) {
				return "flmall_item_"+siteCode.toLowerCase();
			}
			if ("en-us".equalsIgnoreCase(languageCode)) {
				return "flmall_item_"+siteCode.toLowerCase();
			} else {
				return "flmall_item_"+siteCode.toLowerCase()+"_"+languageCode.toLowerCase();
			}
		} else {
			return "flmall_item_"+siteCode.toLowerCase();
		}

	}

	/**
	 * 添加/修改/删除/同步数据 索引名
	 */
	public String getIndexName(String siteCode, String languageCode) {
		String baseIndexName = getBaseIndexName(siteCode);
		if (baseIndexName == null) {
			return null;
		}
		if (siteCode.startsWith("MY") || siteCode.startsWith("SG")) {
			if (StringUtils.isBlank(languageCode) || "en-us".equalsIgnoreCase(languageCode)) {
				return baseIndexName;
			} else {
				return baseIndexName + "_" + languageCode.toLowerCase();
			}
		}
		return baseIndexName;
	}

	private String getBaseIndexName(String siteCode) {
		if (siteCode.startsWith("MY") || siteCode.startsWith("SG")) {
			return "flmall_item_" + siteCode.toLowerCase() + "_v2";
		}
		return SITE_CODE_TO_INDEX.get(siteCode);
	}


	public List<Record> getTestItems(String siteCode){
		String sqlPara = "select id,categoryId,name title,marketPrice,salePrice,picUrl,currencycode,sellItemId sid,labelList,keywordList "
				+ "from test_item where esSync = 0 and status = 1 and deleteFlag = 0 and publishStatus = 1 and siteCodeCode = ? limit 10";
		return new ArrayList<>();
	}

	public Record getTestItems(String siteCode,int id){
		String sqlPara = "select id,categoryId,name title,marketPrice,salePrice,picUrl,currencycode,sellItemId sid,labelList,keywordList "
				+ "from test_item where status = 1 and deleteFlag = 0 and publishStatus = 1 and siteCodeCode = ? and id = ?";
		return null;
	}

	public List<Record> getTestItemsAttr(int id){
		String sqlPara = "select attrPath,attrValue "
				+ "from test_item_attr where itemId = ? ";
		return new ArrayList<>();
	}
}
