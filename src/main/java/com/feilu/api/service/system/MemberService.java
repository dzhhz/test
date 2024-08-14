package com.feilu.api.service.system;

import com.alibaba.fastjson.JSONArray;
import com.feilu.api.common.entity.Ret;
import com.mybatisflex.core.paginate.Page;

import java.math.BigDecimal;
import java.util.Map;

public interface MemberService {
    /**
     * 获取意见反馈列表
     * @param siteCode 站点
     * @param memberId 会员id
     * @param pageNumber 页码数
     * @param pageSize 分页大小
     * @return 意见反馈列表
     */
    Page<Map<String, Object>> getFeedbacks(String siteCode, String memberId, int pageNumber, int pageSize);

    /**
     * 保存意见反馈列表
     * @param memberId 会员id
     * @param siteCode 站点
     * @param opinion 文字内存
     * @param imageList 图片列表
     * @return 保存结果
     */
    Ret saveFeedback(String memberId, String siteCode, String opinion, JSONArray imageList);

    /**
     * 获取钱包总额
     * @param siteCode 站点
     * @param currencyCode 货币代码
     * @param memberId 会员id
     * @return 钱包总额
     */
    BigDecimal getTotalWalletAmount(String siteCode, String currencyCode, String memberId);

    /**
     * 分页获取钱包记录
     * @param siteCode 站点
     * @param currencyCode 货币代码
     * @param memberId 会员id
     * @param pageNumber 页码数
     * @param pageSize 分页大小
     * @return 分页结果
     */
    Page<Map<String, Object>> pageWalletRecordList(String siteCode, String currencyCode, String memberId, int pageNumber, int pageSize);

    /**
     * 保存钱包记录
     * @param memberId 会员id
     * @param siteCode 站点
     * @param currencyCode 货币代码
     * @param amount 操作钱数
     * @param optType 操作类型 +  or -
     * @param type 类型
     * @param status 状态
     * @param orderId 订单id
     * @return 操作结果
     */
    Ret saveWalletRecord(String memberId, String siteCode, String currencyCode, BigDecimal amount, String optType, String type, String status, String orderId);

    /**
     * 更新钱包总金额
     * @param memberId 会员id
     * @param currencyCode 货币代码
     * @param optType 操作类型
     * @param amount 金额
     */
    void updateWalletAmount(String memberId, String currencyCode, String optType, BigDecimal amount);

    /**
     * 更新钱包操作记录
     * @param orderId 订单id
     * @param status 状态
     */
    void updateWalletRecord(String orderId, String status);
}
