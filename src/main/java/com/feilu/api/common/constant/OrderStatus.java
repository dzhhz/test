package com.feilu.api.common.constant;

public class OrderStatus {

	/**
	 * 已关闭
	 */
	public static final String ORDER_STATUS_CLOSED = "CLOSED";

	/**
	 * 已作废
	 * orderItems/orderExt 作废/客户取消/订单超时统一用
	 */
	public static final String ORDER_STATUS_CANCEL = "CANCEL";

	/**
	 * 已取消
	 * 客户操作
	 */
	public static final String ORDER_STATUS_CANCELED = "CANCELED";

	/**
	 * 等待验证
	 */
	public static final String ORDER_STATUS_VERIFYING = "UNVERIFY";

	/**
	 * 订单提交成功
	 */
	public static final String ORDER_STATUS_SUBMITED = "SUBMITED";

	/**
	 * 待支付
	 */
	public static final String ORDER_STATUS_UNPAID = "UNPAID";

	/**
	 * 已支付
	 */
	public static final String ORDER_STATUS_PAID = "PAID";

	/**
	 * 正在出库
	 */
	public static final String ORDER_STATUS_OUTSTOCK = "OUTSTOCK";

	/**
	 * 正在配送
	 */
	public static final String ORDER_STATUS_DELIVERYING = "DELIVERYING";

	/**
	 * 待收货
	 */
	public static final String ORDER_STATUS_RECEIVING = "RECEIVING";

	/**
	 * 已签收
	 */
	public static final String ORDER_STATUS_RECEIVED = "RECEIVED";

	/**
	 * 已拒收
	 */
	public static final String ORDER_STATUS_REJECT = "REJECT";

	/**
	 * 备货中
	 */
	public static final String ORDER_STATUS_STOCKING = "STOCKING";
	
	/**
	 * 处理中
	 */
	public static final String ORDER_STATUS_PROCESSING = "PROCESSING";
	
	/**
	 * 运输中
	 */
	public static final String ORDER_STATUS_SHIPPED = "SHIPPED";
	
	/**
	 * 清关中
	 */
	public static final String ORDER_STATUS_PASSING = "PASSING";
	
	/**
	 * 待评论
	 */
	public static final String ORDER_STATUS_REVIEWING = "REVIEWING";
	
	/**
	 * 已退款
	 */
	public static final String ORDER_STATUS_REFUND= "REFUNDED";
	
	/**
	 * 超时关闭
	 */
	public static final String ORDER_STATUS_TIMEOUTCLOSED = "TIMEOUTCLOSED";
	
}
