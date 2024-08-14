package com.feilu.api.common.service;

import com.feilu.api.common.config.DynamicDatasourceHolder;
import com.feilu.api.dao.system.mapper.SystemItemMapper;
import com.feilu.api.dao.system.mapper.SystemItemRankingMapper;
import com.feilu.api.dao.website.entity.SiteBaseSitecode;
import com.feilu.api.dao.website.mapper.SiteBaseSitecodeMapper;
import com.google.cloud.bigquery.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 推荐服务
 */
@Component
public class RecommendService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendService.class);

    private static final Map<String, DataModel> dataModelMap = new HashMap<>();

    private static final Map<String, BiMap<String, Long>> deviceIdMap = new HashMap<>();

    /**
     * 推荐页面类型 4种
     */
    public static final String HOME = "home";
    public static final String MINE = "mine";
    public static final String DETAIL = "detail";
    public static final String ORDER_SUCCESS = "orderSuccess";

    /**
     * 推荐方法 5个
     */
    public static final String MODEL = "rec_model";
    public static final String NEW = "rec_new";
    public static final String RANKING = "rec_rank";
    public static final String CATEGORY = "rec_cat";
    public static final String LIST = "rec_list";

    private SiteBaseSitecodeMapper siteBaseSitecodeMapper;
    private SystemItemMapper systemItemMapper;
    private SystemItemRankingMapper systemItemRankingMapper;

    @Autowired
    public void setSiteBaseSitecodeMapper(SiteBaseSitecodeMapper siteBaseSitecodeMapper) {
        this.siteBaseSitecodeMapper = siteBaseSitecodeMapper;
    }

    @Autowired
    public void setSystemItemMapper(SystemItemMapper systemItemMapper) {
        this.systemItemMapper = systemItemMapper;
    }

    @Autowired
    public void setSystemItemRankingMapper(SystemItemRankingMapper systemItemRankingMapper) {
        this.systemItemRankingMapper = systemItemRankingMapper;
    }


    /**
     * 通过商品总榜推荐
     * @param siteCode 站点
     * @param itemId 当前itemId
     * @param ageGroupFlag 年龄组
     * @param sexFlag 性别
     * @param categoryIds 分类
     * @param pageNumber 页数
     * @param count 推荐书
     * @return itemIds
     */
    public List<Integer> recommendItemByRanking(String siteCode, Integer itemId, String ageGroupFlag, Integer sexFlag, String categoryIds, int pageNumber, int count) {
        return systemItemRankingMapper.selectRankedItems(siteCode, itemId, ageGroupFlag, sexFlag, categoryIds, count, (pageNumber - 1) * count);
    }


    /**
     * 推荐新品
     * @param siteCode 站点
     * @param pageNumber 页数
     * @param count 推荐数
     * @return 商品ids
     */
    public List<Integer> recommendNewItem(String siteCode, int pageNumber, int count) {
        return systemItemMapper.queryNewItem(siteCode, count, (pageNumber - 1) * count);
    }

    /**
     * 只根据用户推荐商品
     * @param deviceIdStr deviceIdStr
     * @param count 推荐个数
     * @return 商品ids
     */
    public List<Integer> recommendItemByUser(String deviceIdStr, int count, String siteCode) {
        List<Integer> itemIds = null;
        try {
            // 初始化
            initDataMoDel(siteCode);

            //判断该站点走不走模型
            if (!dataModelMap.containsKey(siteCode)) {
                return itemIds;
            }

            DataModel dataModel = dataModelMap.get(siteCode);
            BiMap<String, Long> deviceIdMapping = deviceIdMap.get(siteCode);

            // 做判断  如果没有就直接不查了 返回一个空列表
            if (!deviceIdMapping.containsKey(deviceIdStr)) {
                return new ArrayList<>();
            }

            // 计算相拟度，相拟度算法很多种，采用基于皮尔逊相关性的相拟度
            UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            // 计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相拟度的邻居，这里使用基于固定数量的邻居
            UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100, similarity, dataModel);

            Long deviceId = deviceIdMapping.get(deviceIdStr);
            // 构建推荐器，基于用户的协同过滤推荐
            Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);
            // 根据用户 推荐商品
            itemIds = recommender.recommend(deviceId, count).stream()
                    .map(t -> Integer.valueOf(String.valueOf(t.getItemID())))
                    .collect(Collectors.toList());
        } catch (TasteException e) {
            logger.error("个人推荐商品报错：{}", e.getMessage(), e);
            itemIds = new ArrayList<>();
        }
        return itemIds;
    }

    /**
     * 根据用户和商品推荐商品
     * @param deviceIdStr deviceIdStr
     * @param itemIdInteger itemId
     * @param count 推荐个数
     * @return 商品ids
     */
    public List<Integer> recommendItemByUerAndItem(String deviceIdStr, Integer itemIdInteger, int count, String siteCode) {
        List<Integer> itemIds = null;
        try {
            // 初始化
            initDataMoDel(siteCode);

            //判断该站点走不走模型
            if (!dataModelMap.containsKey(siteCode)) {
                return itemIds;
            }

            DataModel dataModel = dataModelMap.get(siteCode);
            BiMap<String, Long> deviceIdMapping = deviceIdMap.get(siteCode);

            if (!deviceIdMapping.containsKey(deviceIdStr)) {
                return new ArrayList<>();
            }
            // 采用基于皮尔逊相关性的相拟度
            ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(dataModel);
            // 基于物品的协同过滤推荐
            GenericItemBasedRecommender genericItemBasedRecommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);

            Long deviceId = deviceIdMapping.get(deviceIdStr);
            long itemId = Long.parseLong(String.valueOf(itemIdInteger));
            // 物品推荐相拟度，计算两个物品同时出现的次数，次数越多任务的相拟度越高
            itemIds = genericItemBasedRecommender.recommendedBecause(deviceId, itemId, count)
                    .stream()
                    .map(t -> Integer.valueOf(String.valueOf(t.getItemID())))
                    .collect(Collectors.toList());
        } catch (TasteException e) {
            logger.error("个人推荐商品报错：{}", e.getMessage(), e);
            itemIds = new ArrayList<>();
        }
        return itemIds;
    }

    /**
     * 根据商品推荐商品
     * @param itemIdInteger itemId
     * @param count 推荐个数
     * @return 商品列表
     */
    public List<Integer> recommendItemByItem(Integer itemIdInteger, int count, String siteCode) {
        List<Integer> itemIds = null;
        try {
            // 初始化
            initDataMoDel(siteCode);

            //判断该站点走不走模型
            if (!dataModelMap.containsKey(siteCode)) {
                return itemIds;
            }

            DataModel dataModel = dataModelMap.get(siteCode);

            // 采用基于皮尔逊相关性的相拟度
            ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(dataModel);
            // 基于物品的协同过滤推荐
            GenericItemBasedRecommender genericItemBasedRecommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
            long itemId = Long.parseLong(String.valueOf(itemIdInteger));
            // 物品推荐相拟度，计算两个物品同时出现的次数，次数越多任务的相拟度越高
            itemIds = genericItemBasedRecommender.mostSimilarItems(itemId, count)
                    .stream()
                    .map(t -> Integer.valueOf(String.valueOf(t.getItemID())))
                    .collect(Collectors.toList());
        } catch (TasteException e) {
            logger.error("个人推荐商品报错：{}", e.getMessage(), e);
            itemIds = new ArrayList<>();
        }
        return itemIds;
    }

    /**
     * 初始化 dataModel
     */
    public synchronized void initDataMoDel(String siteCode) {
        // 只有当 dataModelMap 为空时才进行初始话
        SiteBaseSitecode siteBaseSitecode = siteBaseSitecodeMapper.selectOneByQuery(new QueryWrapper().eq("siteCode", siteCode));
        // 如果loadModelFlag为1才加载模型
        if (siteBaseSitecode != null && siteBaseSitecode.getLoadModelFlag() != null && siteBaseSitecode.getLoadModelFlag() == 1) {
            DataModel dataModel = dataModelMap.get(siteCode);
            if (dataModel != null) {
                return;
            }
            // 构建当前站点数据模型
            buildDataMoDel(siteCode);
        }
    }

    /**
     * 构建 dataModel
     */
    public synchronized void buildDataMoDel(String siteCode) {


        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 获取前两个月的日期
        LocalDate twoMonthsAgoDate = currentDate.minusMonths(2);
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 格式化日期
        String endTime = currentDate.format(formatter);
        String startTime = twoMonthsAgoDate.format(formatter);

        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        if (siteCode != null) {
            // 初始化指定站点模型
            build(bigquery, siteCode, startTime, endTime);
        } else {
            // 获取需要加载模型的站点  定时任务
            DynamicDatasourceHolder.setDataSource("site");
            List<SiteBaseSitecode> sitecodeList = siteBaseSitecodeMapper.selectListByQuery(new QueryWrapper().eq("loadModelFlag", 1));
            if (sitecodeList != null && !sitecodeList.isEmpty()) {
                for (SiteBaseSitecode siteBaseSitecode : sitecodeList) {
                    if (StringUtils.isNotBlank(siteBaseSitecode.getSiteCode())) {
                        build(bigquery, siteBaseSitecode.getSiteCode(), startTime, endTime);
                    }

                }
            }
        }
    }

    public void build(BigQuery bigquery, String siteCode, String startTime, String endTime) {
        String querySql = "SELECT deviceId, itemId, sum(viewDetail) viewDetailNum, sum(addToCart) addToCartNum, sum(purchase) orderNum" +
                " FROM `august-circle-335808.ds_site_event.tb_track_event_device_day` " +
                " WHERE deviceId IS  NOT NULL AND siteCode = @siteCode AND eventDate >= DATETIME(@startTime) and eventDate < DATETIME(@endTime)"  + " GROUP BY deviceId, itemId ";
        QueryJobConfiguration queryJobConfiguration = QueryJobConfiguration.newBuilder(querySql)
                .addNamedParameter("siteCode", QueryParameterValue.string(siteCode))
                .addNamedParameter("startTime", QueryParameterValue.string(startTime))
                .addNamedParameter("endTime", QueryParameterValue.string(endTime))
                .build();
        TableResult query = null;
        try {
            query = bigquery.query(queryJobConfiguration);
        } catch (InterruptedException e) {
            logger.error("buildDataMoDel error", e);
            throw new RuntimeException(e);
        }

        FastByIDMap<PreferenceArray> fastByIdMap = new FastByIDMap<>();

        // 创建字符串到long的映射
        AtomicLong deviceIdCounter = new AtomicLong();
        BiMap<String, Long> newDeviceIdMapping = HashBiMap.create();

        // 用于存储所有用户的偏好数据
        Map<Long, List<GenericPreference>> userPreferencesMap = new HashMap<>();
        query.iterateAll().forEach(t -> {
            String deviceIdStr = t.get("deviceId").getStringValue();
            String itemIdStr = t.get("itemId").getStringValue();
            long viewDetailNum = t.get("viewDetailNum").getLongValue();
            long addToCartNum = t.get("addToCartNum").getLongValue();
            long orderNum = t.get("orderNum").getLongValue();

            // 将字符串deviceId和productId映射为long类型
            newDeviceIdMapping.putIfAbsent(deviceIdStr, deviceIdCounter.getAndIncrement());

            long deviceId = newDeviceIdMapping.get(deviceIdStr);
            long productId = Long.parseLong(itemIdStr);

            // 计算偏好值
            float preferenceValue = 0.1f * viewDetailNum + 0.3f * addToCartNum + 0.6f * orderNum;

            // 将偏好数据添加到用户偏好列表中
            userPreferencesMap.computeIfAbsent(deviceId, k -> new ArrayList<>())
                    .add(new GenericPreference(deviceId, productId, preferenceValue));
        });

        // 将用户偏好列表转换为PreferenceArray并放入fastByIdMap中
        userPreferencesMap.forEach((userId, preferences) -> {
            PreferenceArray userPreferenceArray = new GenericUserPreferenceArray(preferences);
            fastByIdMap.put(userId, userPreferenceArray);
        });
        deviceIdMap.put(siteCode, newDeviceIdMapping);
        // 构建DataModel
        DataModel dataModel = new GenericDataModel(fastByIdMap);
        dataModelMap.put(siteCode, dataModel);
    }
}
