package com.feilu.api.dao.website.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.website.entity.SiteOrder;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 *  映射层。
 *
 * @author dzh
 * @since 2024-08-08
 */
@Mapper
public interface SiteOrderMapper extends BaseMapper<SiteOrder> {

    
    Map<String, Object> selectOrderInfoById(@Param("orderId") String orderId);
}
