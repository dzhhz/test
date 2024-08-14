package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  映射层。
 *
 * @author dzh
 * @since 2024-08-08
 */
@Mapper
public interface SystemItemMapper extends BaseMapper<SystemItem> {

    List<Integer> queryNewItem(@Param("siteCode") String siteCode,
                               @Param("limit") Integer limit,
                               @Param("offset") Integer offset);

}
