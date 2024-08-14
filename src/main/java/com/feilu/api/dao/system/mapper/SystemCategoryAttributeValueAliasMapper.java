package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemCategoryAttributeValueAlias;
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
public interface SystemCategoryAttributeValueAliasMapper extends BaseMapper<SystemCategoryAttributeValueAlias> {

    
    List<Map<String, Object>> selectAttributeValues(@Param("attrId") Integer attrId,
                                                    @Param("languageCode") String languageCode);
}
