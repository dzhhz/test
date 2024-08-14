package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemOrderAftersaleReason;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *  映射层。
 *
 * @author dzh
 * @since 2024-08-09
 */
@Mapper
public interface SystemOrderAftersaleReasonMapper extends BaseMapper<SystemOrderAftersaleReason> {

    
    List<Map<String, Object>> selectWithReasonType(@Param("languageCode") String languageCode,
                                                   @Param("reasonType") String reasonType);
}
