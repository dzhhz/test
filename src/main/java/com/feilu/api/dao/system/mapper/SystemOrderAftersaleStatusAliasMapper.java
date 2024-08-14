package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemOrderAftersaleStatusAlias;
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
public interface SystemOrderAftersaleStatusAliasMapper extends BaseMapper<SystemOrderAftersaleStatusAlias> {

    String selectStatusName(@Param("languageCode") String languageCode,
                            @Param("statusType")Integer statusType,
                            @Param("status")Integer status);

    
    List<Map<String, Object>> selectStatusInfo(@Param("languageCode") String languageCode,
                                               @Param("statusType")Integer statusType,
                                               @Param("nowStatus")Integer nowStatus);

    
    Map<String, Object> selectTitle(@Param("languageCode") String languageCode,
                                    @Param("status")Integer status);
}
