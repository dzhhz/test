package com.feilu.api.common.entity;

import co.elastic.clients.elasticsearch.core.search.HitsMetadata;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("all")
public class PageModelDTO implements Serializable {

    private static final long serialVersionUID = 192274358354304398L;
    /**
     * 数据总条数
     */
    private Long total;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 条数
     */
    private Integer pageSize = 10;

    /**
     * 界定返回值类型 json;obj;
     */
    private String resultType = "string";

    /**
     * 是否分页
     */
    private Boolean isPage = true;

    /**
     * json格式返回值
     */
    private List<String> jsonRsList = new ArrayList<String>();

    /**
     * 直接从ES获取的结果
     */
    private HitsMetadata hitsMetadata;

    /**
     * 区间查询参数
     */
    private Map<String, RangConditionDTO> rangConditionMap = new HashMap<>();

    /**
     * 时间区间查询参数
     */
    private Map<String, RangConditionsToTimeModelDTO> rangConditionsToTimeModelMap = new HashMap<>();

    /**
     * 模糊相等查询条件，多个查询条件","进行切割
     */
    private Map<String, Object> likeSearchCondition = new HashMap<>();

    /**
     * or条件查询
     */
    private Map<String, Object> orSearchCondition = new HashMap<>();

    /**
     * or条件查询
     */
    private Map<String, Object> orLikeSearchCondition = new HashMap<>();

    /**
     * or条件查询集合类操作
     */
    private List<Map<String, Object>> orSearchConditionList = new ArrayList<>();

    /**
     * 相等查询条件，多个查询条件","进行切割
     */
    private Map<String, Object> equalsSearchCondition = new HashMap<>();

    /**
     * in 查询
     */
    private Map<String, List> inSearchCondition = new HashMap<>();

    /**
     * 模糊不相等的条件，多个查询条件","进行切割
     */
    private Map<String, Object> noLikeSearchConditioin = new HashMap<>();

    /**
     * 不相等的条件，多个查询条件","进行切割
     */
    private Map<String, Object> noEqualsSearchConditioin = new HashMap<>();

    /**
     * 为空过滤
     */
    private List<String> isNullConditioin = new ArrayList<>();

    /**
     * 不为空过滤
     */
    private List<String> isNotNullConditioin = new ArrayList<>();

    /**
     * 排序字段，关键字asc，desc
     */
    private Map<String, String> sortFileds = new LinkedHashMap<>();

    /**
     * 排序字段集合，方便对排序顺序的控制 关键字asc，desc
     */
    private List<Map<String, String>> sortFiledsList = new ArrayList<>();

    /**
     * 高亮字段
     */
    private List<String> hightFieldList = new ArrayList<>();

    /**
     * 去重字段
     */
    private String collapseField;

    /**
     * 指定查询结果包含的字段
     */
    private String[] fetchSourceIncludes;

    /**
     * 指定查询结果不包含的字段
     */
    private String[] fetchSourceExcludes;

    /**
     * 分词字段
     */
    private Map<String, Object> analyzersField = new HashMap<>();

    public String getSortFileds(String key) {
        return sortFileds.get(key);
    }

    public RangConditionDTO getRangConditionMap(String key) {
        return rangConditionMap.get(key);
    }

    public RangConditionsToTimeModelDTO getRangConditionsToTimeModelMap(String key) {
        return rangConditionsToTimeModelMap.get(key);
    }

    public void setJsonRsList(String json) {
        this.jsonRsList.add(json);
    }

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public Boolean getIsPage() {
		return isPage;
	}

	public void setIsPage(Boolean isPage) {
		this.isPage = isPage;
	}

	public List<String> getJsonRsList() {
		return jsonRsList;
	}

	public void setJsonRsList(List<String> jsonRsList) {
		this.jsonRsList = jsonRsList;
	}

	public HitsMetadata getHitsMetadata() {
		return hitsMetadata;
	}

	public void setHitsMetadata(HitsMetadata hitsMetadata) {
		this.hitsMetadata = hitsMetadata;
	}

	public Map<String, RangConditionDTO> getRangConditionMap() {
		return rangConditionMap;
	}

	public void setRangConditionMap(Map<String, RangConditionDTO> rangConditionMap) {
		this.rangConditionMap = rangConditionMap;
	}

	public Map<String, RangConditionsToTimeModelDTO> getRangConditionsToTimeModelMap() {
		return rangConditionsToTimeModelMap;
	}

	public void setRangConditionsToTimeModelMap(Map<String, RangConditionsToTimeModelDTO> rangConditionsToTimeModelMap) {
		this.rangConditionsToTimeModelMap = rangConditionsToTimeModelMap;
	}

	public Map<String, Object> getLikeSearchCondition() {
		return likeSearchCondition;
	}

	public void setLikeSearchCondition(Map<String, Object> likeSearchCondition) {
		this.likeSearchCondition = likeSearchCondition;
	}

	public Map<String, Object> getOrSearchCondition() {
		return orSearchCondition;
	}

	public void setOrSearchCondition(Map<String, Object> orSearchCondition) {
		this.orSearchCondition = orSearchCondition;
	}

	public Map<String, Object> getOrLikeSearchCondition() {
		return orLikeSearchCondition;
	}

	public void setOrLikeSearchCondition(Map<String, Object> orLikeSearchCondition) {
		this.orLikeSearchCondition = orLikeSearchCondition;
	}

	public List<Map<String, Object>> getOrSearchConditionList() {
		return orSearchConditionList;
	}

	public void setOrSearchConditionList(List<Map<String, Object>> orSearchConditionList) {
		this.orSearchConditionList = orSearchConditionList;
	}

	public Map<String, Object> getEqualsSearchCondition() {
		return equalsSearchCondition;
	}

	public void setEqualsSearchCondition(Map<String, Object> equalsSearchCondition) {
		this.equalsSearchCondition = equalsSearchCondition;
	}

	public Map<String, List> getInSearchCondition() {
		return inSearchCondition;
	}

	public void setInSearchCondition(Map<String, List> inSearchCondition) {
		this.inSearchCondition = inSearchCondition;
	}

	public Map<String, Object> getNoLikeSearchConditioin() {
		return noLikeSearchConditioin;
	}

	public void setNoLikeSearchConditioin(Map<String, Object> noLikeSearchConditioin) {
		this.noLikeSearchConditioin = noLikeSearchConditioin;
	}

	public Map<String, Object> getNoEqualsSearchConditioin() {
		return noEqualsSearchConditioin;
	}

	public void setNoEqualsSearchConditioin(Map<String, Object> noEqualsSearchConditioin) {
		this.noEqualsSearchConditioin = noEqualsSearchConditioin;
	}

	public List<String> getIsNullConditioin() {
		return isNullConditioin;
	}

	public void setIsNullConditioin(List<String> isNullConditioin) {
		this.isNullConditioin = isNullConditioin;
	}

	public List<String> getIsNotNullConditioin() {
		return isNotNullConditioin;
	}

	public void setIsNotNullConditioin(List<String> isNotNullConditioin) {
		this.isNotNullConditioin = isNotNullConditioin;
	}

	public Map<String, String> getSortFileds() {
		return sortFileds;
	}

	public void setSortFileds(Map<String, String> sortFileds) {
		this.sortFileds = sortFileds;
	}

	public List<Map<String, String>> getSortFiledsList() {
		return sortFiledsList;
	}

	public void setSortFiledsList(List<Map<String, String>> sortFiledsList) {
		this.sortFiledsList = sortFiledsList;
	}

	public List<String> getHightFieldList() {
		return hightFieldList;
	}

	public void setHightFieldList(List<String> hightFieldList) {
		this.hightFieldList = hightFieldList;
	}

	public String getCollapseField() {
		return collapseField;
	}

	public void setCollapseField(String collapseField) {
		this.collapseField = collapseField;
	}

	public String[] getFetchSourceIncludes() {
		return fetchSourceIncludes;
	}

	public void setFetchSourceIncludes(String[] fetchSourceIncludes) {
		this.fetchSourceIncludes = fetchSourceIncludes;
	}

	public String[] getFetchSourceExcludes() {
		return fetchSourceExcludes;
	}

	public void setFetchSourceExcludes(String[] fetchSourceExcludes) {
		this.fetchSourceExcludes = fetchSourceExcludes;
	}

	public Map<String, Object> getAnalyzersField() {
		return analyzersField;
	}

	public void setAnalyzersField(Map<String, Object> analyzersField) {
		this.analyzersField = analyzersField;
	}

}
