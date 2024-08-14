package com.feilu.api.service.system.impl;

import com.feilu.api.common.entity.Ret;
import com.feilu.api.common.utils.MapUtils;
import com.feilu.api.dao.system.entity.SystemCategory;
import com.feilu.api.dao.system.mapper.*;
import com.feilu.api.service.system.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private SystemCategoryMapper systemCategoryMapper;
    private SystemCategoryAttributeAliasMapper systemCategoryAttributeAliasMapper;
    private SystemCategoryAttributeValueAliasMapper systemCategoryAttributeValueAliasMapper;

    @Autowired
    public void setSystemCategoryMapper(SystemCategoryMapper systemCategoryMapper) {
        this.systemCategoryMapper = systemCategoryMapper;
    }

    @Autowired
    public void setSystemCategoryAttributeAliasMapper(SystemCategoryAttributeAliasMapper systemCategoryAttributeAliasMapper) {
        this.systemCategoryAttributeAliasMapper = systemCategoryAttributeAliasMapper;
    }

    @Autowired
    public void setSystemCategoryAttributeValueAliasMapper(SystemCategoryAttributeValueAliasMapper systemCategoryAttributeValueAliasMapper) {
        this.systemCategoryAttributeValueAliasMapper = systemCategoryAttributeValueAliasMapper;
    }

    /**
     * 查询分类聚合属性
     * @param languageCode 语言代码
     * @param categoryId 分类id
     * @return 结果
     */
    @Override
    public Ret queryCategoryAttrs(String languageCode, Integer categoryId) {
        Ret ret = Ret.create();
        Set<Integer> parentSet = new LinkedHashSet<>();

        if (categoryId != 1) {
            // 查询所有的父类 id
            Integer parentId = categoryId;
            while (parentId != 1) {
                parentSet.add(parentId);
                parentId = getParentId(parentId);
            }
            parentSet.add(1); // 确保根节点被包含
        }

        List<Map<String, Object>> attrList = new ArrayList<>();
        if (categoryId == 1) {
            attrList = systemCategoryAttributeAliasMapper.selectDistinctAttributes(languageCode, List.of(categoryId));
        } else {
            // 查询所有父类去重后类目
           attrList = systemCategoryAttributeAliasMapper.selectDistinctAttributes(languageCode, new ArrayList<>(parentSet));
        }

        if (!attrList.isEmpty()) {
            for (Map<String, Object> attr : attrList) {
                // 查询属性值
                List<Map<String, Object>> attrValueList = systemCategoryAttributeValueAliasMapper.selectAttributeValues(MapUtils.getIntegerValue(attr, "attrKey"), languageCode);
                attr.put("attrValueList", attrValueList);
            }
        }

        if (attrList.size() > 1) {
            attrList.sort(Comparator.comparingInt(o -> MapUtils.getIntValue(o, "attrKey")));
        }

        ret.set("attrList", attrList);
        return ret;
    }

    /**
     * 获取父分类
     * @param categoryId 分类id
     * @return 父分类id
     */
    private Integer getParentId(Integer categoryId) {
        SystemCategory systemCategory = systemCategoryMapper.selectOneById(categoryId);
        return systemCategory == null ? 1 : systemCategory.getParentId();
    }

}
