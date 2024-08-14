package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemOrderItems;
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
public interface SystemOrderItemsMapper extends BaseMapper<SystemOrderItems> {

    
    List<Map<String, Object>> selectIsSyncByOrderId(@Param("orderId") String orderId,
                                                    @Param("languageCode") String languageCode);
    
    Map<String, Object> selectWithOrderIdAndItemId(@Param("orderId") String orderId,
                                                   @Param("itemId") Integer itemId,
                                                   @Param("languageCode") String languageCode);
}
