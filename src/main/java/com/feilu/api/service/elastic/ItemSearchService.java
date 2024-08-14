package com.feilu.api.service.elastic;

import com.feilu.api.common.entity.ApiResponse;
import com.feilu.api.common.entity.EsItemReq;
import com.feilu.api.common.entity.EsItemResp;
import com.feilu.api.common.entity.Ret;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {

    /**
     * 搜索商品
     */
    ApiResponse<?> searchItems(EsItemReq esItemReq);

    /**
     * 推荐商品
     */
    ApiResponse<?> recommendItems(EsItemReq esItemReq);

    /**
     * 推荐商品 new
     */
    ApiResponse<?> recommendItemsNew(EsItemReq esItemReq);

    /**
     * 相似商品
     */
    ApiResponse<?> querySimilarItems(EsItemReq esItemReq);

    /**
     * 模型里面的推荐商品
     */
    List<EsItemResp> queryModelItems(EsItemReq esItemReq);

    /**
     * 聚合数据返回分类categoryIds
     */
    List<String> queryCategoryIds(EsItemReq esItemReq);

    /**
     * 添加单条数据
     */
    Ret addData(String siteCode, String languageCode, Integer id, String paramJson);

    /**
     * 更新数据
     */
    Ret updateData(String siteCode, String languageCode, Integer id, String paramJson);

    /**
     * 删除单条数据
     */
    Ret deleteData(String siteCode, String languageCode, Integer indexId);

    /**
     * ES 批量保存数据（同步）
     **/
    Ret saveDataBatch(String siteCode, String languageCode);

    /**
     * 更新指定字段数据
     */
    Ret updateAppointData(String siteCode,String languageCode,Integer id,String[] updateFiles,String[] updateFilesValues);

    /**
     * 批量修改数据
     */
    Ret updateDataBatch(String siteCode, String languageCode);

    /**
     * 聚合数据返回kv
     */
    Map<String, Object> queryAggregation(String keywords, String siteCode, String languageCode);
}
