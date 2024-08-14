package com.feilu.api.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PriceFormatUtils {

	private static ThreadLocal<DecimalFormat> threadLocal = ThreadLocal.withInitial(()->{
        return new DecimalFormat("#,###,##0");
    });

	public static String format(String text) {
		return threadLocal.get().format(new BigDecimal(text));
	}

	public static String formatDiscount(String markPrice,String salePrice) {
		BigDecimal discount = (new BigDecimal(markPrice).subtract(new BigDecimal(salePrice))).divide(new BigDecimal(markPrice),2,BigDecimal.ROUND_HALF_UP);
		discount = discount.multiply(new BigDecimal("100")).setScale(0);
		return "-"+discount+"%";
	}
}
