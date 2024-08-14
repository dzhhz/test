package com.feilu.api.service.system;

import com.feilu.api.common.entity.Ret;
import com.feilu.api.dao.system.entity.SystemOrder;
import com.feilu.api.dao.system.entity.SystemOrderAftersale;
import com.mybatisflex.core.paginate.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {

    /**
     * 分页获取会员订单
     * @param siteCode 站点
     * @param memberId 会员id
     * @param orderStatus 订单状态
     * @param pageSize 页码数
     * @param pageNumber 分页大小
     * @return 分页订单列表
     */
    Page<Map<String, Object>> getMemberOrders(String siteCode, String memberId, String mobile, String orderStatus, int pageSize, int pageNumber);

    /**
     * 根据id获取订单信息
     * @param siteCode 站点
     * @param memberId 会员id
     * @param orderId 订单id
     * @return 订单信息
     */
    Map<String, Object> getMemberOrderById(String siteCode, String memberId, String mobile, String orderId);

    /**
     * 获取订单商品列表
     * @param siteCode 站点
     * @param orderId 订单id
     * @return 商品列表
     */
    List<Map<String, Object>> getOrderItemList(String siteCode, String orderId);

    /**
     * 获取订单收货人信息
     * @param orderId 订单id
     * @return 收货人信息
     */
    Map<String, Object> getOrderConsignee(String orderId);

    /**
     * 订单数量统计
     * @param siteCode 站点
     * @param memberId 会员id
     * @param mobile mobile
     * @return 订单数量统计
     */
    Map<String, Object> getMyOrderCount(String siteCode, String memberId, String mobile);

    /**
     * 根据id获取订单信息
     * @param orderId 订单id
     * @return 订单信息
     */
    SystemOrder getMemberOrder(String orderId);

    /**
     * 订单更新
     * @param orderId 订单id
     * @param reviewStatus 状态
     * @param payStatus 支付状态
     * @param orderStatus 订单状态
     * @param paymentId 支付id
     * @param status 状态
     */
    void updateOrder(String orderId,Integer reviewStatus,Integer payStatus,String orderStatus,Integer paymentId,Integer status);

    /**
     * 获取订单追踪信息
     * @param orderId 订单id
     * @param language 语言代码
     * @param siteCode 站点
     * @return 信息
     */
    Ret getOrderTrackInfos(String orderId, String language, String siteCode);

    /**
     * 获取订单售后处理原因
     * @param reasonType 原因类型
     * @param languageCode 语言代码
     * @return 列表
     */
    List<Map<String, Object>> getAftersaleReason(String reasonType,String languageCode);

    /**
     * 订单售后处理保存
     * @param siteCode 站点
     * @param languageCode 语言
     * @param currencyCode 货币
     * @param id id
     * @param orderId 订单id
     * @param itemId 商品id
     * @param itemSkuId sku
     * @param memberId 会员id
     * @param imgProof 图片
     * @param processType 处理类型
     * @param applyNum num
     * @param applyAmount amount
     * @param reasonCode 原因代码
     * @param applyReason 原因
     * @param applyDes des
     * @param isReceive 是否接收
     * @return 结果
     */
    Integer saveOrderAftersale(String siteCode, String languageCode, String currencyCode, Integer id, String orderId, Integer itemId, Integer itemSkuId, String memberId, String imgProof, Integer processType, Integer applyNum, BigDecimal applyAmount, String reasonCode, String applyReason, String applyDes, Integer isReceive);

    /**
     * 取消订单售后处理
     * @param siteCode 站点
     * @param itemId 商品id
     * @param itemSkuId sku
     * @param orderId 订单id
     * @param id id
     * @param memberId 会员id
     */
    void cancelAftersale(String siteCode,Integer itemId,Integer itemSkuId,String orderId,Integer id,String memberId);

    /**
     * 订单售后删除图片
     * @param siteCode 站点
     * @param itemId 商品id
     * @param itemSkuId sku
     * @param orderId 订单id
     * @param id id
     * @param memberId 会员id
     * @param imgProof 图片
     */
    void deleteAftersaleImage(String siteCode,Integer itemId,Integer itemSkuId,String orderId,Integer id,String memberId,String imgProof);

    /**
     * 更新售后处理
     * @param siteCode 站点
     * @param languageCode 语言代码
     * @param itemId 商品id
     * @param itemSkuId sku
     * @param orderId 订单id
     * @param id id
     * @param memberId 会员id
     * @param expressNo 快递编号
     * @param expressName 快递名称
     */
    void updateAftersaleExpress(String siteCode,String languageCode,Integer itemId,Integer itemSkuId,String orderId,Integer id,String memberId,String expressNo,String expressName);

    /**
     * 获取售后信息
     * @param siteCode 站点代码
     * @param languageCode 语言代码
     * @param itemId 商品id
     * @param itemSkuId sku
     * @param orderId 订单id
     * @param id id
     * @param memberId 会员id
     * @return 售后信息
     */
    Map<String, Object> getAftersaleInfo(String siteCode,String languageCode,Integer itemId,Integer itemSkuId,String orderId,Integer id,String memberId);

    /**
     * 分页获取售后列表
     * @param siteCode 站点
     * @param memberId 会员id
     * @param pageSize 分页大小
     * @param pageNumber 页码数
     * @return
     */
    Page<Map<String, Object>> getAftersaleList(String siteCode, String memberId, int pageSize, int pageNumber);

    /**
     * 订单售后申请数量
     * @param siteCode 站点
     * @param itemId 商品id
     * @param itemSkuId sku
     * @param orderId 订单id
     * @param memberId 会员id
     * @return 结果
     */
    SystemOrderAftersale getAftersaleApplyNum(String siteCode, Integer itemId, Integer itemSkuId, String orderId, String memberId);

    /**
     * 获取订单状态
     * @param siteCode 站点
     * @param orderId 订单id
     * @param memberId 会员id
     * @return 状态
     */
    String getOrderStatus(String siteCode, String orderId, String memberId);

    /**
     * 获取订单商品状态
     * @param siteCode 站点
     * @param orderId 订单id
     * @param memberId 会员id
     * @return 状态
     */
    String getOrderItemsStatus(String siteCode, String orderId, String memberId);

    /**
     * 一定时间内 用户的订单 指定SKU的 数量
     * @param itemSkuId sku
     * @param memberId 会员id
     * @param beginTime 开始额时间
     * @param endTime 结束时间
     * @return 数量
     */
    int getOrderItemSkuNumByTime(Integer itemSkuId, String memberId, Date beginTime, Date endTime);

    /**
     * 通过手机号查询有效的已下单最新用户
     * @param siteCode 站点
     * @param mobile mobile
     * @return
     */
    String getOneMemberIdByPhone(String siteCode, String mobile);

    /**
     * 通过手机号处理订单优惠
     * @param siteCode 站点
     * @param mobile mobile
     * @param memberId 会员id
     * @param couponId 优惠券id
     * @param reason 原因
     * @param remark 备注
     * @param optUserId 操作用户id
     * @return 结果
     */
    Ret handleOrderCouponByPhone(String siteCode, String mobile, String memberId, Integer couponId, String reason, String remark,Integer optUserId);


    /**
     * 保存订单日志
     * @param memberId 会员id
     * @param mobile mobile
     * @param orderId 订单id
     * @param statusFlag 状态标志
     * @param sysOrderStatus 系统订单状态
     * @param orderStatus 订单状态
     * @param reason 原因
     * @param remark 备注
     * @param optUserId 操作用户id
     */
    void saveOrderLog(String memberId, String mobile,String orderId, String statusFlag, String sysOrderStatus, String orderStatus, String reason, String remark,Integer optUserId);

    /**
     * redis 同步订单数据
     * @param orderId 订单id
     */
    public void syncOrder(String orderId);
}
