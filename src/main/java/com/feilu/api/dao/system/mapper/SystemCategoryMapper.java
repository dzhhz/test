package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemCategory;
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
public interface SystemCategoryMapper extends BaseMapper<SystemCategory> {


    
    List<Map<String, Object>> selectMemberWalletRecords(
            @Param("memberId") String memberId,
            @Param("siteCode") String siteCode,
            @Param("currencyCode") String currencyCode,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );
}
