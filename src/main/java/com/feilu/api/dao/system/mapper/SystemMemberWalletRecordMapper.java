package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemMemberWalletRecord;
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
public interface SystemMemberWalletRecordMapper extends BaseMapper<SystemMemberWalletRecord> {

    
    List<Map<String, Object>> pageMemberWalletRecord(@Param("memberId") String memberId,
                                                     @Param("siteCode") String siteCode,
                                                     @Param("currencyCode") String currencyCode,
                                                     @Param("limit") int limit,
                                                     @Param("offset") int offset
    );

    Integer countMemberWalletRecord(@Param("memberId") String memberId,
                                    @Param("siteCode") String siteCode,
                                    @Param("currencyCode") String currencyCode);
}
