package com.feilu.api.dao.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.feilu.api.dao.system.entity.SystemMemberFeedback;
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
public interface SystemMemberFeedbackMapper extends BaseMapper<SystemMemberFeedback> {

    
    public List<Map<String, Object>> page(@Param("memberId") String memberId,
                          @Param("languageCode") String languageCode,
                          @Param("limit") int limit,
                          @Param("offset") int offset);
}
