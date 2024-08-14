package com.feilu.api.common.utils;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feilu.api.common.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Slf4j
@Component
public class EsUtils {

    private static ElasticsearchTransport transport;

    @Value("${es.host}")
    private String host;

    @Value("${es.port}")
    private Integer port;

    /**
     * 初始化索引 todo
     *
     * @param indexName    索引名称
     * @param settingsJson settings数据
     * @param mappingJson  mapping数据
     */
    public boolean initIndex(String indexName, String settingsJson, String mappingJson) {

        ElasticsearchClient client = getEsClient();
        // 判断索引是否存在
        boolean result = isIndexExists(client, indexName);
        if (result) {
            log.error("索引已存在: {}", indexName);
            closeEsTransport();
            return false;
        }
        CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder().index(indexName).build();
        closeEsTransport();
        return true;
    }

    /**
     * 添加单条数据
     *
     * @param indexName 索引名
     * @param indexId   索引id 默认uuid
     * @param paramJson json数据
     */
    public boolean addData(String indexName, String indexId, String paramJson) {
        ElasticsearchClient client = getEsClient();
        boolean success = false;

        try {
            // 判断索引是否存在
            if (!isIndexExists(client, indexName)) {
                log.error("索引不存在: {}", indexName);
                return false;
            }

            // 解析 JSON 文档并创建 IndexRequest
            JSONObject document = JSONObject.parseObject(paramJson);
            IndexRequest<Object> request = new IndexRequest.Builder<>()
                    .index(indexName)
                    .id(indexId)
                    .document(document)
                    .build();
            // 执行索引操作
            IndexResponse response = client.index(request);
            // 分析响应结果
            Result result = response.result();
            if (result.equals(Result.Created) || result.equals(Result.Updated)) {
                success = true;
            } else {
                log.error("向索引添加数据失败: 索引: {}, 索引ID: {}", indexName, indexId);
            }
        } catch (IOException e) {
            log.error("索引: {}, 索引ID: {} 保存异常: ", indexName, indexId, e);
        } finally {
            closeEsTransport();
        }

        return success;
    }

    /**
     * ES 批量保存数据（同步）
     *
     * @param indexName:      索引名称
     * @param primaryKeyName: 主键名称
     * @param paramListJson:  数据集合JSON
     * @return 批量保存结果
     **/
    public boolean saveDataBatch(String indexName, String primaryKeyName, String paramListJson) {
        ElasticsearchClient client = getEsClient();
        boolean success = false;

        try {
            // 判断索引是否存在
            if (!isIndexExists(client, indexName)) {
                log.error("索引不存在: {}", indexName);
                return false;
            }

            // 构建批量操作
            BulkRequest.Builder builder = new BulkRequest.Builder();
            JSONArray jsonArray = JSONArray.parseArray(paramListJson);
            for (Object o : jsonArray) {
                JSONObject document = (JSONObject) o;
                builder.operations(op -> op.index(idx -> idx
                        .index(indexName)
                        .id(document.getString(primaryKeyName))
                        .document(document)
                ));
            }

            // 执行批量操作
            BulkResponse response = client.bulk(builder.build());

            // 处理响应中的错误
            if (response.errors()) {
                for (BulkResponseItem item : response.items()) {
                    log.error("索引: {}, 主键: {} 批量保存数据失败: {}", indexName, primaryKeyName, item.error());
                }
                return false;
            }

            // 记录索引新增与修改数量
            int createdCount = 0;
            int updatedCount = 0;

            for (BulkResponseItem item : response.items()) {
                if (Result.Created.jsonValue().equals(item.result())) {
                    createdCount++;
                } else if (Result.Updated.jsonValue().equals(item.result())) {
                    updatedCount++;
                }
            }

            log.info("索引: {}, 主键: {} 批量同步更新成功, 共新增 {} 个, 修改 {} 个", indexName, primaryKeyName, createdCount, updatedCount);
            success = true;
        } catch (IOException e) {
            log.error("索引: {}, 主键: {} 批量保存数据异常: ", indexName, primaryKeyName, e);
        } finally {
            closeEsTransport();
        }

        return success;
    }

    /**
     * ES 批量保存数据（异步）
     *
     * @param indexName:      索引名称
     * @param primaryKeyName: 主键名称
     * @param paramListJson:  数据集合JSON
     * @return 批量保存结果
     **/

    public boolean saveDataBatchAsync(String indexName, String primaryKeyName, String paramListJson) {
        ElasticsearchAsyncClient client = getEsAsyncClient();
        boolean success = false;

        try {
            // 构建批量操作
            BulkRequest.Builder builder = new BulkRequest.Builder();
            JSONArray jsonArray = JSONArray.parseArray(paramListJson);
            for (Object o : jsonArray) {
                JSONObject document = (JSONObject) o;
                builder.operations(op -> op.index(idx -> idx
                        .index(indexName)
                        .id(document.getString(primaryKeyName))
                        .document(document)
                ));
            }

            // 执行批量操作
            CompletableFuture<BulkResponse> bulkResponseFuture = client.bulk(builder.build());

            // 等待结果并处理
            BulkResponse response = bulkResponseFuture.get();

            // 处理响应中的错误
            if (response.errors()) {
                for (BulkResponseItem item : response.items()) {
                    log.error("索引: {}, 主键: {} 批量保存数据失败: {}", indexName, primaryKeyName, item.error());
                }
                return false;
            }

            // 记录索引新增与修改数量
            int createdCount = 0;
            int updatedCount = 0;

            for (BulkResponseItem item : response.items()) {
                if (Result.Created.jsonValue().equals(item.result())) {
                    createdCount++;
                } else if (Result.Updated.jsonValue().equals(item.result())) {
                    updatedCount++;
                }
            }

            log.info("索引: {}, 主键: {} 批量同步更新成功, 共新增 {} 个, 修改 {} 个", indexName, primaryKeyName, createdCount, updatedCount);
            success = true;
        } catch (InterruptedException | ExecutionException e) {
            log.error("索引: {}, 主键: {} 批量保存数据异常: ", indexName, primaryKeyName, e);
        } finally {
            closeEsTransport();
        }

        return success;
    }


    /**
     * 更新数据
     * @param indexName 索引名
     * @param indexId	索引id
     * @param paramJson  json数据
     */
    public boolean updateData(String indexName, String indexId, String paramJson) {
        ElasticsearchClient client = getEsClient();
        try {
            // 判断索引是否存在
            boolean indexExists = isIndexExists(client, indexName);
            if (!indexExists) {
                log.error("索引不存在: {}", indexName);
                return false;
            }

            // 构建更新请求
            UpdateRequest.Builder<Object, Object> builder = new UpdateRequest.Builder<>();
            JSONObject document = JSONObject.parseObject(paramJson);
            builder.index(indexName)
                    .id(indexId)
                    .docAsUpsert(true)
                    .doc(document);

            // 执行更新操作
            UpdateResponse<Object> response = client.update(builder.build(), Object.class);

            // 处理响应结果
            return switch (response.result()) {
                case Created -> {
                    log.info("索引: {}, 索引ID: {} 新增成功", indexName, indexId);
                    yield true;
                }
                case Updated -> {
                    log.info("索引: {}, 索引ID: {} 修改成功", indexName, indexId);
                    yield true;
                }
                case NoOp -> {
                    log.info("索引: {}, 索引ID: {} 无变化", indexName, indexId);
                    yield true;
                }
                default -> {
                    log.warn("索引: {}, 索引ID: {} 更新结果未知: {}", indexName, indexId, response.result());
                    yield false;
                }
            };
        } catch (Exception e) {
            log.error("索引: {}, 索引ID: {} 更新数据异常: ", indexName, indexId, e);
            return false;
        }  finally {
            closeEsTransport();
        }
    }

    /**
     * 更新指定字段数据
     * @param indexName 索引名
     * @param indexId	索引id
     * @param paramJson  json数据
     * @param strArr  指定字段
     */
    public boolean updateAppointData(String indexName,String indexId,String paramJson,String[] strArr){

        ElasticsearchClient client = getEsClient();
        try {
            // 判断索引是否存在
            boolean indexExists = isIndexExists(client, indexName);
            if (!indexExists) {
                log.error("索引不存在: {}", indexName);
                return false;
            }

            // 解析 JSON 对象
            JSONObject document = JSONObject.parseObject(paramJson);
            // 仅保留指定字段
            JSONObject filteredDocument = new JSONObject();
            for (String field : strArr) {
                if (document.containsKey(field)) {
                    filteredDocument.put(field, document.get(field));
                }
            }
            // 构建更新请求
            UpdateRequest.Builder<Object, Object> builder = new UpdateRequest.Builder<>();
            builder.index(indexName)
                    .id(indexId)
                    .docAsUpsert(true)
                    .doc(filteredDocument);

            // 执行更新操作
            UpdateResponse<Object> response = client.update(builder.build(), Object.class);

            // 处理响应结果
            return switch (response.result()) {
                case Created -> {
                    log.info("索引: {}, 索引ID: {} 新增成功", indexName, indexId);
                    yield true;
                }
                case Updated -> {
                    log.info("索引: {}, 索引ID: {} 修改成功", indexName, indexId);
                    yield true;
                }
                case NoOp -> {
                    log.info("索引: {}, 索引ID: {} 无变化", indexName, indexId);
                    yield true;
                }
                default -> {
                    log.warn("索引: {}, 索引ID: {} 更新结果未知: {}", indexName, indexId, response.result());
                    yield false;
                }
            };
        } catch (Exception e) {
            log.error("索引: {}, 索引ID: {} 更新数据异常: ", indexName, indexId, e);
            return false;
        }  finally {
            closeEsTransport();
        }
    }

    /**
     * ES 批量修改数据
     *
     * @param indexName:      索引名称
     * @param primaryKeyName: 主键名称
     * @param paramListJson:  数据集合JSON
     * @return 批量修改结果
     **/
    public boolean updateDataBatch(String indexName, String primaryKeyName, String paramListJson) {

        ElasticsearchClient client = getEsClient();
        try {
            // 创建批量请求

            BulkRequest.Builder builder = new BulkRequest.Builder();

            JSONArray jsonArray = JSONArray.parseArray(paramListJson);
            for (Object o : jsonArray) {
                JSONObject document = (JSONObject) o;
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(o);
                builder.operations(op -> op.update(idx -> idx
                        .index(indexName)
                        .id(document.getString(primaryKeyName))
                         .withJson(new StringReader(json))));
            }

            BulkResponse response = client.bulk(builder.build());
            // 处理响应中的错误
            if (response.errors()) {
                for (BulkResponseItem item : response.items()) {
                    log.error("索引: {}, 主键: {} 批量保存数据失败: {}", indexName, primaryKeyName, item.error());
                }
                closeEsTransport();
                return false;
            }

            // 记录索引新增与修改数量
            int createdCount = 0;
            int updatedCount = 0;

            for (BulkResponseItem item : response.items()) {
                if (Result.Created.jsonValue().equals(item.result())) {
                    createdCount++;
                } else if (Result.Updated.jsonValue().equals(item.result())) {
                    updatedCount++;
                }
            }

            log.info("索引: {}, 主键: {} 批量同步更新成功, 共新增 {} 个, 修改 {} 个", indexName, primaryKeyName, createdCount, updatedCount);
            return true;
        } catch (Exception e) {
            log.error("索引: {}, 主键: {} 批量保存数据异常: ", indexName, primaryKeyName, e);
        } finally {
            closeEsTransport();
        }
        return false;
    }


    /**
     * ES 批量修改指定数据
     *
     * @param indexName:      索引名称
     * @param primaryKeyName: 主键名称
     * @param paramListJson:  数据集合JSON
     * @param strArr: 		      指定字段
     * @return 批量修改结果
     **/
    public boolean updateAppointDataBatch(String indexName, String primaryKeyName, String paramListJson, String[] strArr) {
        ElasticsearchClient client = getEsClient();
        try {
            // 创建批量请求

            BulkRequest.Builder builder = new BulkRequest.Builder();

            JSONArray jsonArray = JSONArray.parseArray(paramListJson);
            for (Object o : jsonArray) {
                JSONObject document = (JSONObject) o;
                // 仅保留指定字段
                JSONObject filteredDocument = new JSONObject();
                for (String field : strArr) {
                    if (document.containsKey(field)) {
                        filteredDocument.put(field, document.get(field));
                    }
                }
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(filteredDocument);
                builder.operations(op -> op.update(idx -> idx
                        .index(indexName)
                        .id(document.getString(primaryKeyName))
                        .withJson(new StringReader(json))));
            }

            BulkResponse response = client.bulk(builder.build());
            // 处理响应中的错误
            if (response.errors()) {
                for (BulkResponseItem item : response.items()) {
                    log.error("索引: {}, 主键: {} 批量保存数据失败: {}", indexName, primaryKeyName, item.error());
                }
                closeEsTransport();
                return false;
            }

            // 记录索引新增与修改数量
            int createdCount = 0;
            int updatedCount = 0;

            for (BulkResponseItem item : response.items()) {
                if (Result.Created.jsonValue().equals(item.result())) {
                    createdCount++;
                } else if (Result.Updated.jsonValue().equals(item.result())) {
                    updatedCount++;
                }
            }

            log.info("索引: {}, 主键: {} 批量同步更新成功, 共新增 {} 个, 修改 {} 个", indexName, primaryKeyName, createdCount, updatedCount);
            return true;
        } catch (Exception e) {
            log.error("索引: {}, 主键: {} 批量保存数据异常: ", indexName, primaryKeyName, e);
        } finally {
            closeEsTransport();
        }
        return false;
    }

    /**
     * ES 删除索引
     *
     * @param indexName: 索引名称
     * @return 操作结果
     **/
    private boolean deleteIndex(String indexName) {
        ElasticsearchClient client = getEsClient();
        // 判断索引是否存在
        boolean result = isIndexExists(client, indexName);
        if (!result) {
            log.error("索引不存在: {}", indexName);
            return false;
        }
        try {
            DeleteIndexResponse deleteIndexResponse = getEsAsyncClient().indices().delete(req -> req.index(indexName)).get();
            if (!deleteIndexResponse.acknowledged()) {
                log.error("索引: {},删除失败", indexName);
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("索引: {}删除索引异常:", indexName, e);
        }
        return false;
    }

    /**
     * ES 根据索引id删除数据
     *
     * @param indexName: 索引名称
     * @param indexId:   索引ID
     * @return 操作结果
     **/
    public boolean deleteDate(String indexName, String indexId) {

        ElasticsearchClient client = getEsClient();
        // 判断索引是否存在
        boolean result = isIndexExists(client, indexName);
        if(!result){
            log.error("索引不存在: {}", indexName);
            return false;
        }

        try {
            DeleteResponse deleteResponse = client.delete(s -> s.index(indexName).id(indexId));

            if (deleteResponse.result() == Result.Deleted) {
                return true;
            } else {
               log.error("索引: {} 下id为 {} ,删除失败", indexName, indexId);
            }
        } catch (IOException e) {
            log.error("索引: {},索引: {}根据索引id删除数据异常: ", indexName, indexId, e);
            return false;
        }
        return false;
    }


    /**
     * ES 批量删除ES索引
     *
     * @param indexName: 索引名称
     * @param indexIds:    索引集合
     * @return 操作结果
     **/
    public boolean bulkDelete(String indexName, List<String> indexIds) {

        ElasticsearchClient client = getEsClient();
        // 判断索引是否存在
        boolean result = isIndexExists(client, indexName);
        if(!result){
            log.error("索引不存在: "+indexName);
            return false;
        }


        try {

            BulkRequest.Builder br = new BulkRequest.Builder();
            for (String indexId : indexIds) {
                br.operations(op -> op
                        .delete(c -> c.id(indexId)));
            }
            BulkResponse response = client.bulk(br.build());
            if (response.errors()) {
                for (BulkResponseItem item : response.items()) {
                    log.error("索引: {},主键: {}批量删除失败:{}", indexName, item.id(), item.result());
                }
                return false;
            }

            // 记录索引新增与修改数量
            int deleteCount = 0;
            for (BulkResponseItem item : response.items()) {
                if (Result.Deleted.jsonValue().equals(item.result())) {
                    deleteCount++;
                }
            }
            log.info("索引: {},批量删除成功,共删除{}个", indexName, deleteCount);
        } catch (IOException e) {
            log.error("索引: {},索引集合: {}批量删除ES索引异常: ", indexName, indexIds, e);
            return false;
        }
        return true;
    }

    /**
     * 获取查询到的聚合数据
     * @param searchResponse        响应结果
     * @param aggregationField 		聚合字段
     */
    public LinkedList<String> getAggregationsData(SearchResponse<Object> searchResponse, String aggregationField) {
        // 存放数据
        LinkedList<String> list = new LinkedList<>();
        Map<String, Aggregate> aggregationsMap = searchResponse.aggregations();
        Aggregate aggregate = aggregationsMap.get(aggregationField);
        StringTermsAggregate terms = aggregate.sterms();
        List<StringTermsBucket> array = terms.buckets().array();
        for (StringTermsBucket stringTermsBucket : array) {
            String value = stringTermsBucket.key().stringValue();
            list.add(value);
        }
        return list;
    }

    /**
     * 获取查询到的List数据不含高亮字段
     */
    public List<EsItemResp> getJsonData(SearchResponse<Object> searchResponse, String tag, String keyWord) {
        List<EsItemResp> list = new ArrayList<>();
        HitsMetadata<Object> HitsMetadata = searchResponse.hits();
        List<Hit<Object>> hits = HitsMetadata.hits();
        if (!hits.isEmpty()) {
            for (Hit<Object> objectHit : hits) {
                assert objectHit.source() != null;
                String string = objectHit.source().toString();
                JSONObject jsonObject = JSONObject.parseObject(string);
                List<EsItemSkuImage> skuImageList;
                skuImageList = JSON.parseArray(jsonObject.getString("skuImageList"), EsItemSkuImage.class);
                EsItemResp esItemResp = JSONObject.parseObject(string, EsItemResp.class);
                esItemResp.setSkuImageList(skuImageList);
                esItemResp.setContentId(esItemResp.getItemId() + "");

                //处理tag(spm)
                if(StringUtils.isNotBlank(tag)) {
                    esItemResp.setTag(tag);
                }else {
                    if(StringUtils.isNotBlank(keyWord)) {
                        esItemResp.setTag("search");
                    }else {
                        esItemResp.setTag("pageList");
                    }
                }
                list.add(esItemResp);
            }
        }
        return list;
    }

    /**
     * 获取查询到的List数据带一个高亮字段
     * @param name			高亮字段
     */
    public List<String> getHighlightJsonData(SearchResponse<Object> searchResponse,String name) {

        List<String> result = new ArrayList<>();
        HitsMetadata<Object> HitsMetadata = searchResponse.hits();
        List<Hit<Object>> hits = HitsMetadata.hits();
        if (!hits.isEmpty()) {
            for (Hit<Object> hit : hits) {
                Map<String, List<String>> highlight = hit.highlight();
                List<String> list = highlight.get(name);
                highlight.put("highlightName", list);
            }
        }
        return result;
    }

    /**
     * 获取数据总数量
     */
    public static int getDataNum(SearchResponse<Object> searchResponse) {
        int count = 0;
        TotalHits total = searchResponse.hits().total();
        if (total != null) {
            count = (int) total.value();
        }
        return count;
    }

    /**
     * 获取查询分页数据
     * @param listJsonData 查询到的List数据
     * @param totalRow 		数据总数量
     * @param pageNumber 	每页数量
     * @param pageSize 		页码
     */
    public ApiResponse getPageData(List listJsonData, int totalRow, int pageNumber, int pageSize, int sort) {
        ApiResponse apiResponse = new ApiResponse();
        Page page = new Page<>();
        int totalPage = (totalRow+pageSize-1)/pageSize;
        page.setList(listJsonData);
        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);
        page.setTotalPage(totalPage);
        page.setTotalRow(totalRow);
        apiResponse.setImageHost("https://img.cdniii.com");
        apiResponse.setLoginStatus(1);
        apiResponse.setState("ok");
        apiResponse.setResult(page);
        return apiResponse;
    }

    /**
     * ES 关闭索引
     *
     * @param indexName: 索引名称
     * @return boolean
     **/
    public boolean closeIndex(String indexName) {
        ElasticsearchClient client = getEsClient();
        // 判断索引是否存在
        if (!isIndexExists(client, indexName)) {
            log.error("索引不存在: " + indexName);
            return false;
        }

        CloseIndexRequest request = new CloseIndexRequest.Builder().index(indexName).build();
        try {
            CloseIndexResponse close = client.indices().close(request);
            if(close.acknowledged()){
                return true;
            }else{
                return false;
            }
        } catch (IOException e) {
            log.error("索引: " + indexName + ",关闭异常:" + e);
            return false;
        }
    }

    /**
     * ES 打开索引
     *
     * @param indexName: 索引名称
     * @return boolean
     **/
    public boolean openIndex(String indexName) {
        ElasticsearchClient client = getEsClient();
        // 判断索引是否存在
        if (!isIndexExists(client, indexName)) {
            log.error("索引不存在: " + indexName);
            return false;
        }

        OpenRequest request = new OpenRequest.Builder().index(indexName).build();
        try {
            OpenResponse open = client.indices().open(request);
            if(open.acknowledged()){
                return true;
            }else{
                return false;
            }
        } catch (IOException e) {
            log.error("索引: "+indexName+",打开异常:"+e);
            return false;
        }
    }

    /**
     * ES 根据主键查询索引名称
     *
     * @param indexName: 索引名称
     * @param indexId:    主键
     * @return 返回数据JSON
     **/
    public String getDataById(String indexName, String indexId) {
        ElasticsearchClient client = getEsClient();
        // 判断索引是否存在
        if (!isIndexExists(client, indexName)) {
            log.error("索引不存在: " + indexName);
            return "false";
        }

        GetRequest getRequest = new GetRequest.Builder().index(indexName).id(indexId).build();
        try {
            GetResponse<Object> getResponse = client.get(getRequest, Object.class);
            Object source = getResponse.source();
            if (source != null) {
                return source.toString();
            } else {
                log.error("索引: {},索引ID: {}根据主键查询索引名称为空", indexName, indexId);
                return "";
            }
        } catch (IOException e) {
            log.error("索引: {},索引ID: {}根据主键查询索引名称异常: ", indexName, indexId, e);
            return "";
        }
    }

    /**
     * 检查索引是否存在
     *
     * @param client    客户端
     * @param indexName 索引名
     * @return 是否存在
     */
    public boolean isIndexExists(ElasticsearchClient client, String indexName) {
        try {
            ExistsRequest existsRequest = new ExistsRequest.Builder().index(indexName).build();
            return client.indices().exists(existsRequest).value();
        } catch (IOException e) {
            log.error("when determining whether the index exists error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 同步客户端；调用结束后，需调用close()关闭transport
     */
    public ElasticsearchClient getEsClient() {
        return new ElasticsearchClient(getEsTransport());
    }

    /**
     * 异步客户端
     */
    public ElasticsearchAsyncClient getEsAsyncClient() {
        return new ElasticsearchAsyncClient(getEsTransport());
    }

    /**
     * 获取 ElasticsearchTransport
     */
    public ElasticsearchTransport getEsTransport() {

        host = StringUtils.isEmpty(host) ? "128.0.0.1" : host;
        port = Objects.isNull(port) ? 9200 : port;

        RestClient restClient = RestClient.builder(
                new HttpHost(host, port)).build();

        transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        return transport;
    }

    /**
     * 关闭 ElasticsearchTransport
     */
    public static void closeEsTransport() {
        if (transport != null) {
            try {
                transport.close();
            } catch (IOException e) {
                log.error("close restClientTransport error: {}", e.getMessage());
            }
        }
    }

}

