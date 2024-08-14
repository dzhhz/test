package com.feilu.api.service.system;

import com.feilu.api.common.entity.Ret;

public interface CategoryService {

    /**
     * 查询分类聚合属性
     * @param languageCode 语言代码
     * @param categoryId 分类id
     * @return 结果
     */
    Ret queryCategoryAttrs(String languageCode, Integer categoryId);
}
