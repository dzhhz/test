package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemCategoryAttributeAlias;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *  映射层。
 *
 * @author dzh
 * @since 2024-08-08
 */
@Mapper
public interface SystemCategoryAttributeAliasMapper extends BaseMapper<SystemCategoryAttributeAlias> {

    
    public List<Map<String, Object>> selectDistinctAttributes(@Param("languageCode") String languageCode,
                                                              @Param("categoryIds") List<Integer> categoryIds);

}
