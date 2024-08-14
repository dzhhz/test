package com.feilu.api.service.system;

import java.util.Map;

public interface ItemService {

    /**
     * 根据页面类型推荐商品
     * @param requestData 请求数据
     * @param type 页面类型
     * @return 商品列表
     */
    Map<String, Object> recommendItemListByType(String requestData, String type);
}
