package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemOrderTrackingRecord;
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
public interface SystemOrderTrackingRecordMapper extends BaseMapper<SystemOrderTrackingRecord> {

    
    List<Map<String,Object>> selectOrderTrackingRecordList(@Param("orderId") String orderId,
                                                           @Param("languageCode") String languageCode);

    
    List<Map<String,Object>> selectListWithParentId(@Param("orderId") String orderId,
                                                    @Param("languageCode") String languageCode,
                                                    @Param("parentId") Integer parentId);
}
