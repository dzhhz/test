package com.feilu.api.common.constant;

import java.math.BigDecimal;

public class SiteIdConstant {

	/**
	 * 不包含的siteId
	 */
	public static final int[] NOT_SITEID = {6};

	/**
	 * 包含的siteId
	 */
	public static final int[] SITEID = {5};

	/**
	 * addPrice
	 */
	public static final BigDecimal addPrice = new BigDecimal(198);

	/**
	 * addDoublePrice
	 */
	public static final BigDecimal addDoublePrice = new BigDecimal(198).multiply(new BigDecimal(2));
	
}
