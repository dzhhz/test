package com.feilu.api.common.constant;

/**
 * 订单追踪状态
 * @author MSI1
 *
 */
public class OrderTrackStatus {

	/**
	 * 已关闭
	 */
	public static final String ORDER_STATUS_CLOSED = "CLOSED";

	/**
	 * 已作废
	 */
	public static final String ORDER_STATUS_CANCEL = "CANCELED";

	/**
	 * 下单成功
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
	 * 备货中
	 */
	public static final String ORDER_STATUS_STOCKING = "STOCKING";

	/**
	 * 出库中
	 */
	public static final String ORDER_STATUS_OUTSTOCK = "OUTSTOCK";

	/**
	 * 仓库质检
	 */
	public static final String ORDER_STATUS_CHECKING = "CHECKING";

	/**
	 * 配货中
	 */
	public static final String ORDER_STATUS_PICKING = "PICKING";

	/**
	 * 已发货
	 */
	public static final String ORDER_STATUS_SHIPPED = "SHIPPED";

	/**
	 * 运输中
	 */
	//public static final String ORDER_STATUS_INTRANSIT = "INTRANSIT";

	/**
	 * 清关中
	 */
	public static final String ORDER_STATUS_PASSING = "PASSING";

	/**
	 * 派送中
	 */
	public static final String ORDER_STATUS_DELIVERYING = "DELIVERYING";

	/**
	 * 待收货
	 */
	public static final String ORDER_STATUS_RECEIVING = "RECEIVING";

	/**
	 * 已签收
	 */
	public static final String ORDER_STATUS_SIGNED = "SIGNED";

	/**
	 * 已拒收
	 */
	public static final String ORDER_STATUS_SENDING = "REJECT";
	
	/**
	 * 下单成功提醒
	 */
	public static final String ORDER_STATUS_ORDER_REMINDER = "ORDER_REMINDER";
	
	/**
	 * 待支付成功提醒
	 */
	public static final String ORDER_STATUS_UNPAID_REMINDER = "UNPAID_REMINDER";
	
	/**
	 * 支付成功提醒
	 */
	public static final String ORDER_STATUS_PAID_REMINDER = "PAID_REMINDER";
	
	/**
	 * 通知仓库备货
	 */
	public static final String ORDER_STATUS_STOCK_REMINDER = "STOCK_REMINDER";
	
	/**
	 * 仓库备货中
	 */
	public static final String ORDER_STATUS_STOCK_STOCKING = "STOCK_STOCKING";
	
	/**
	 * 出库提醒
	 */
	public static final String ORDER_STATUS_OUTSTOCK_REMINDER = "OUTSTOCK_REMINDER";
	
	/**
	 * 运输提醒
	 */
	public static final String ORDER_STATUS_TRANSIT_REMINDER = "TRANSIT_REMINDER";
	
	/**
	 * 清关提醒
	 */
	public static final String ORDER_STATUS_PASSING_REMINDER = "PASSING_REMINDER";
	
	/**
	 * 派送提醒
	 */
	public static final String ORDER_STATUS_DELIVERY_REMINDER = "DELIVERY_REMINDER";

	/**
	 * 已退款
	 */
	public static final String ORDER_STATUS_REFUND= "REFUNDED";
	
	/**
	 * 退款成功提醒
	 */
	public static final String ORDER_STATUS_REFUND_REMINDER= "REFUND_REMINDER";
	
	/**
	 * 已关闭提醒
	 */
	public static final String ORDER_STATUS_CLOSED_REMINDER = "CLOSED_REMINDER";
	
}
