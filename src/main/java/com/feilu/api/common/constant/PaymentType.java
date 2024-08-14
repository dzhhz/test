package com.feilu.api.common.constant;

public class PaymentType {

	/**
	 * 货到付款
	 */
	public static final int COD = 1;
	
	/**
	 * paypal
	 */
	public static final int PAYPAL = 2;
	
	/**
	 * braintree
	 * 信用卡
	 */
	public static final int BRAINTREE_CREDIT_CARD = 3;
	
	/**
	 * braintree 
	 * paypal
	 */
	public static final int BRAINTREE_PAYPAL = 4;
	
	/**
	 * 现金
	 */
	public static final int CASH = 5;
}
