package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemOrder;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 *  映射层。
 *
 * @author dzh
 * @since 2024-08-08
 */
@Mapper
public interface SystemOrderMapper extends BaseMapper<SystemOrder> {

    
    List<Map<String, Object>> pageOrders(@Param("siteCode") String siteCode,
                                         @Param("memberId") String memberId,
                                         @Param("mobile") String mobile,
                                         @Param("orderStatus") String orderStatus,
                                         @Param("limit") int limit,
                                         @Param("offset") int offset);

    Integer countOrders(@Param("siteCode") String siteCode,
                        @Param("memberId") String memberId,
                        @Param("mobile") String mobile,
                        @Param("orderStatus") String orderStatus);

    SystemOrder selectOrderWithMobile(@Param("orderId") String orderId,
                                      @Param("mobile") String mobile,
                                      @Param("memberId") String memberId);

    Map<String, Object> selectOrderCount(@Param("siteCode") String siteCode,
                                         @Param("memberId") String memberId);

    List<String> selectProcessOrderIds(@Param("siteCode") String siteCode,
                                       @Param("memberId") String memberId,
                                       @Param("mobile") String mobile);

    List<String> selectShippedOrderIds(@Param("siteCode") String siteCode,
                                       @Param("memberId") String memberId,
                                       @Param("mobile") String mobile);

    Map<String, Object> selectOrderInfoById(@Param("orderId") String orderId);


    String selectOneMemberIdByPhone(@Param("siteCode") String siteCode,
                                    @Param("mobile") String mobile);

    String selectRecentOrderByPhone(@Param("siteCode") String siteCode,
                                    @Param("mobile") String mobile,
                                    @Param("orderStatus") String orderStatus,
                                    @Param("date") LocalDateTime date);
}
