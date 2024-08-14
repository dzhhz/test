package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemItemRanking;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  映射层。
 *
 * @author dzh
 * @since 2024-08-08
 */
@Mapper
public interface SystemItemRankingMapper extends BaseMapper<SystemItemRanking> {

    List<Integer> selectRankedItems(@Param("siteCode") String siteCode,
                                   @Param("itemId") Integer itemId,
                                   @Param("ageGroupFlag") String ageGroupFlag,
                                   @Param("sexFlag") Integer sexFlag,
                                   @Param("categoryIds") String categoryIds,
                                   @Param("limit") int limit,
                                   @Param("offset") int offset
    );
}
