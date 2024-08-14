package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemOrderAftersale;
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
public interface SystemOrderAftersaleMapper extends BaseMapper<SystemOrderAftersale> {

    
    List<Map<String, Object>> pageOrderAfterSale(@Param("siteCode") String siteCode,
                                                 @Param("memberId") String memberId,
                                                 @Param("mobile") String mobile,
                                                 @Param("limit") int limit,
                                                 @Param("offset") int offset);

    Integer countOrderAfterSale(@Param("siteCode") String siteCode,
                                @Param("memberId") String memberId,
                                @Param("mobile") String mobile);

    
    List<Map<String, Object>> selectAfterSaleItems(@Param("id") String id,
                                                   @Param("orderId") String orderId,
                                                   @Param("itemId") Integer itemId,
                                                   @Param("itemSkuId") Integer itemSkuId,
                                                   @Param("languageCode") String languageCode);

    
    Map<String, Object> selectAfterSaleInfo(@Param("id") String id,
                                            @Param("orderId") String orderId,
                                            @Param("itemId") Integer itemId,
                                            @Param("itemSkuId") Integer itemSkuId,
                                            @Param("memberId") String memberId);

}
