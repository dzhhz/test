package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemOrderTrackingStatus;
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
public interface SystemOrderTrackingStatusMapper extends BaseMapper<SystemOrderTrackingStatus> {

    
    Map<String, Object> selectTrackingWithStatus(@Param("languageCode") String languageCode,
                                                 @Param("statusCode") String statusCode);

    
    List<Map<String, Object>> selectTrackingWithParentId(@Param("languageCode") String languageCode,
                                                         @Param("parentId") Integer parentId);

    
    Map<String, Object> selectTrackingStatus(@Param("languageCode") String languageCode,
                                             @Param("statusCode") String statusCode);
}
