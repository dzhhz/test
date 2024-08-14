package com.feilu.api.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feilu.api.common.entity.Ret;
import com.feilu.api.common.annotation.ValidateRequestData;
import com.feilu.api.dao.system.entity.SystemOrder;
import com.feilu.api.dao.system.entity.SystemOrderAftersale;
import com.feilu.api.service.system.MemberService;
import com.feilu.api.service.system.OrderService;
import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("sysOrder")
public class OrderController {

	private OrderService orderService;
	private MemberService memberService;


	@Autowired
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	@Autowired
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	/**
	 * 订单列表
	 */
	@PostMapping("getMemberOrders")
	@ValidateRequestData
	public Ret getMemberOrders(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String orderStatus = map.getString("orderStatus");
		String memberId = map.getString("memberId");
		String mobile = map.getString("mobile");
		Integer pageNumber = map.getInteger("pageNumber");
		int pageSize = 10;
		Page<Map<String, Object>> page = orderService.getMemberOrders(siteCode, memberId, mobile, orderStatus,pageSize,pageNumber);
		return Ret.ok("result", page);
	}

	/**
	 * 订单详情
	 */
	@PostMapping("getOrderDetail")
	@ValidateRequestData
	public Ret getOrderDetail(@RequestBody Map<String,Object> requestParam) {
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String memberId = map.getString("memberId");
		String orderId = map.getString("orderId");
		String mobile = map.getString("mobile");
		Map<String, Object> order = orderService.getMemberOrderById(siteCode, memberId, mobile, orderId);
		Ret ret = new Ret();
		ret.set("order",order);
		if(order != null){
			List<Map<String, Object>> orderItemList = orderService.getOrderItemList(siteCode,orderId);
			Map<String, Object> orderConsignee = orderService.getOrderConsignee(orderId);
			ret.set("orderItemList",orderItemList);
			ret.set("orderConsignee",orderConsignee);
		}
		return ret;
	}

	/**
	 * 订单数量统计
	 * 钱包总金额
	 */
	@PostMapping("getMyOrderCount")
	@ValidateRequestData
	public Ret getMyOrderCount(@RequestBody Map<String,Object> requestParam) {
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String currencyCode = map.getString("currencyCode");
		String memberId = map.getString("memberId");
		String mobile = map.getString("mobile");
		Map<String, Object> record = orderService.getMyOrderCount(siteCode,memberId,mobile);
		BigDecimal totalWalletAmount = memberService.getTotalWalletAmount(siteCode,currencyCode, memberId);
		record.put("totalWalletAmount",totalWalletAmount);
		return Ret.ok("result", record);
	}

	/**
	 * 订单
	 */
	@PostMapping("getMemberOrder")
	@ValidateRequestData
	public Ret getMemberOrder(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String orderId = map.getString("orderId");
		SystemOrder systemOrder = orderService.getMemberOrder(orderId);
		return Ret.ok("result", systemOrder);
	}

	/**
	 * 订单更新
	 */
	@PostMapping("updateOrder")
	@ValidateRequestData
	public Ret updateOrder(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String orderId = map.getString("orderId");
		Integer reviewStatus = map.getInteger("reviewStatus");
		Integer payStatus = map.getInteger("payStatus");
		String orderStatus = map.getString("orderStatus");
		Integer paymentId = map.getInteger("paymentId");
		Integer status = map.getInteger("status");

		orderService.updateOrder(orderId,reviewStatus,payStatus,orderStatus,paymentId,status);
		return Ret.ok().set("msg","同步更新成功");

	}

	/**
	 * 订单追踪信息
	 */
	@PostMapping("getOrderTrackInfos")
	@ValidateRequestData
	public Ret getOrderTrackInfos(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String orderId = map.getString("orderId");
		String language = map.getString("language");
		String siteCode = map.getString("siteCode");
		Ret ret = orderService.getOrderTrackInfos(orderId,language,siteCode);
		return ret;
	}

	/**
	 * 订单售后处理原因
	 */
	@PostMapping("getAftersaleReason")
	@ValidateRequestData
	public Ret getAftersaleReason(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String reasonType = map.getString("reasonType");
		String languageCode = map.getString("languageCode");
		List<Map<String, Object>> aftersaleReason = orderService.getAftersaleReason(reasonType,languageCode);
		Ret ret = Ret.ok();
		ret.set("result", aftersaleReason);
		return ret;
	}

	/**
	 * 订单售后处理保存
	 */
	@PostMapping("saveOrderAftersale")
	@ValidateRequestData
	public Ret saveOrderAftersale(@RequestBody Map<String,Object> requestParam){
		log.info("[申请售后服务请求参数]" + requestParam.get("requestData"));
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String languageCode = map.getString("languageCode");
		String currencyCode = map.getString("currencyCode");
		Integer id = map.getInteger("id");
		String orderId = map.getString("orderId");
		Integer itemId = map.getInteger("itemId");
		Integer itemSkuId = map.getInteger("itemSkuId");
		String memberId = map.getString("memberId");
		String imgProof = map.getString("imgProof");
		Integer processType = map.getInteger("processType");
		Integer applyNum = map.getInteger("applyNum");
		BigDecimal applyAmount = new BigDecimal(map.getString("applyAmount"));
		String reasonCode = map.getString("reasonCode");
		String applyReason = map.getString("applyReason");
		String applyDes = map.getString("applyDes");
		Integer isReceive = map.getInteger("isReceive");

		if (!processType.equals(4)) {
			log.error("用户正在申请售后服务，订单: "+orderId);

			if ("zh-TW".equals(languageCode)) {
				return  Ret.fail("message","訂單無法申請售後，請聯系客服處理");
			}
			if ("th-TH".equals(languageCode)) {
				return Ret.fail("message","ไม่สามารถยื่นขอใช้บริการหลังการขายได้ กรุณาติดต่อฝ่ายบริการลูกค้าเพื่อดำเนินกา");
			}
			if ("vn-VN".equals(languageCode)) {
				return Ret.fail("message","vui lòng liên hệ bộ phận chăm sóc khách hàng để được xử lý");
			}
			return Ret.fail("message","Please contact customer service for processing");
		}

		int aftersaleId = orderService.saveOrderAftersale(siteCode,languageCode,currencyCode,id,orderId,itemId,itemSkuId,memberId,imgProof,processType,applyNum,applyAmount,reasonCode,applyReason,applyDes,isReceive);
		return Ret.ok().set("message","ok").set("id", aftersaleId);
	}

	/**
	 * 订单售后处理
	 * 取消
	 */
	@PostMapping("cancelAftersale")
	@ValidateRequestData
	public Ret cancelAftersale(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		Integer id = map.getInteger("id");
		String orderId = map.getString("orderId");
		String memberId = map.getString("memberId");
		Integer itemId = map.getInteger("itemId");
		Integer itemSkuId = map.getInteger("itemSkuId");

		orderService.cancelAftersale(siteCode,itemId,itemSkuId,orderId,id,memberId);
		return Ret.ok().set("message","ok");
	}

	/**
	 * 订单售后处理
	 * 删除图片
	 */
	@PostMapping("deleteAftersaleImage")
	@ValidateRequestData
	public Ret deleteAftersaleImage(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		Integer id = map.getInteger("id");
		String orderId = map.getString("orderId");
		String memberId = map.getString("memberId");
		Integer itemId = map.getInteger("itemId");
		Integer itemSkuId = map.getInteger("itemSkuId");
		String imgProof = map.getString("imgProof");
		orderService.deleteAftersaleImage(siteCode,itemId,itemSkuId,orderId,id,memberId,imgProof);
		return Ret.ok().set("message","ok");
	}

	/**
	 * 订单售后处理更新
	 */
	@PostMapping("updateAftersaleExpress")
	@ValidateRequestData
	public Ret updateAftersaleExpress(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String languageCode = map.getString("languageCode");
		Integer id = map.getInteger("id");
		String orderId = map.getString("orderId");
		Integer itemId = map.getInteger("itemId");
		Integer itemSkuId = map.getInteger("itemSkuId");
		String memberId = map.getString("memberId");
		String expressNo = map.getString("expressNo");
		String expressName = map.getString("expressName");

		orderService.updateAftersaleExpress(siteCode,languageCode,itemId,itemSkuId,orderId,id,memberId,expressNo,expressName);
		return Ret.ok().set("message","ok");
	}

	/**
	 * 订单售后处理详情
	 */
	@PostMapping("getAftersaleInfo")
	@ValidateRequestData
	public Ret getAftersaleInfo(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String languageCode = map.getString("languageCode");
		Integer id = map.getInteger("id");
		String orderId = map.getString("orderId");
		String memberId = map.getString("memberId");
		Integer itemId = map.getInteger("itemId");
		Integer itemSkuId = map.getInteger("itemSkuId");
		Map<String, Object> record = orderService.getAftersaleInfo(siteCode,languageCode,itemId,itemSkuId,orderId,id,memberId);
		return Ret.ok().set("result", record);
	}

	/**
	 * 订单售后处理
	 * 列表
	 */
	@PostMapping("getAftersaleList")
	@ValidateRequestData
	public Ret getAftersaleList(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String memberId = map.getString("memberId");
		Integer pageNumber = map.getInteger("pageNumber");
		Integer pageSize = map.getInteger("pageSize");
		Page<Map<String, Object>> page = orderService.getAftersaleList(siteCode,memberId,pageSize,pageNumber);
        return Ret.ok("result", page);
	}

	/**
	 * 订单售后
	 * 申请数量
	 */
	@PostMapping("getAftersaleApplyNum")
	@ValidateRequestData
	public Ret getAftersaleApplyNum(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String orderId = map.getString("orderId");
		String memberId = map.getString("memberId");
		Integer itemId = map.getInteger("itemId");
		Integer itemSkuId = map.getInteger("itemSkuId");
		SystemOrderAftersale record = orderService.getAftersaleApplyNum(siteCode,itemId,itemSkuId,orderId,memberId);
		return Ret.ok().set("result", record);
	}

	/**
	 * 订单状态
	 *
	 */
	@PostMapping("getOrderStatus")
	@ValidateRequestData
	public Ret getOrderStatus(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String orderId = map.getString("orderId");
		String memberId = map.getString("memberId");
		String orderStatus = orderService.getOrderStatus(siteCode,orderId,memberId);
		String orderItemStatus = orderService.getOrderItemsStatus(siteCode,orderId,memberId);
		return Ret.ok().set("orderStatus", orderStatus).set("orderItemStatus", orderItemStatus);
	}

	/**
	 * 一定时间内 用户的订单指定SKU的数量
	 */
	@PostMapping("getOrderItemSkuNumByTime")
	@ValidateRequestData
	public Ret getOrderItemSkuNumByTime(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		Integer itemSkuId = map.getInteger("itemSkuId");
		String memberId = map.getString("memberId");
		Date beginTime = map.getDate("beginTime");
		Date endTime = map.getDate("endTime");
		int itemSkuNum = orderService.getOrderItemSkuNumByTime(itemSkuId,memberId,beginTime,endTime);
		return Ret.ok().set("itemSkuNum", itemSkuNum);
	}

	/**
	 * 	通过手机号查询有效的已下单最新用户
	 */
	@PostMapping("getOneMemberIdByPhone")
	@ValidateRequestData
	public Ret getOneMemberIdByPhone(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String phone = map.getString("phone");
		String memberId = orderService.getOneMemberIdByPhone(siteCode,phone);
		return Ret.ok().set("memberId", memberId);
	}

	/**
	 * 	通过手机号处理订单优惠
	 */
	@PostMapping("handleOrderCouponByPhone")
	@ValidateRequestData
	public Ret handleOrderCouponByPhone(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String siteCode = map.getString("siteCode");
		String mobile = map.getString("mobile");
		String memberId = map.getString("memberId");
		String reason = map.getString("reason");
		String remark = map.getString("remark");
		Integer optUserId = map.getInteger("optUserId");
		Integer couponId = map.getInteger("couponId");
        return orderService.handleOrderCouponByPhone(siteCode, mobile, memberId, couponId, reason, remark, optUserId);
	}

	/**
	 * 	保存订单日志
	 */
	@PostMapping("saveOrderLog")
	@ValidateRequestData
	public Ret saveOrderLog(@RequestBody Map<String,Object> requestParam){
		JSONObject map = JSON.parseObject(requestParam.get("requestData").toString());
		String mobile = map.getString("mobile");
		String memberId = map.getString("memberId");
		String orderId = map.getString("orderId");
		String statusFlag = map.getString("statusFlag");
		String sysOrderStatus = map.getString("sysOrderStatus");
		String orderStatus = map.getString("orderStatus");
		String reason = map.getString("reason");
		String remark = map.getString("remark");
		Integer optUserId = map.getInteger("optUserId");
		orderService.saveOrderLog( memberId, mobile, orderId, statusFlag, sysOrderStatus, orderStatus, reason, remark, optUserId);
		return Ret.ok().set("message", "success");
	}

}
