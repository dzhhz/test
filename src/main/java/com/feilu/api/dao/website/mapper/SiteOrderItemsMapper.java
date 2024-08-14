package com.feilu.api.dao.website.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.website.entity.SiteOrderItems;
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
public interface SiteOrderItemsMapper extends BaseMapper<SiteOrderItems> {

    
    List<Map<String, Object>> selectNotSyncByOrderId(@Param("orderId") String orderId);

    
    List<Map<String, Object>> selectItemsByOrderId(@Param("orderId") String orderId);
}
