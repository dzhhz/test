package com.feilu.api.common.kit;

import com.feilu.api.common.config.DynamicDatasourceHolder;
import com.feilu.api.dao.website.entity.SiteBaseCurrency;
import com.feilu.api.dao.website.entity.SiteBaseSitecode;
import com.feilu.api.dao.website.mapper.SiteBaseCurrencyMapper;
import com.feilu.api.dao.website.mapper.SiteBaseLanguageOrderstatusMapper;
import com.feilu.api.dao.website.mapper.SiteBaseSitePaymentMapper;
import com.feilu.api.dao.website.mapper.SiteBaseSitecodeMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Component
public class FormatKit {

	private static ApplicationContext applicationContext;

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		FormatKit.applicationContext = applicationContext;
	}

	public static SiteBaseSitecode getSiteByCurrency(String currencyCode) {
		try {
			DynamicDatasourceHolder.setDataSource("site");
			SiteBaseSitecodeMapper siteBaseSitecodeMapper = applicationContext.getBean(SiteBaseSitecodeMapper.class);
			return siteBaseSitecodeMapper.selectOneByQuery(new QueryWrapper().eq("currencyCode", currencyCode));
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	public static SiteBaseSitecode getSiteBySiteCode(String siteCode) {
		try {
			DynamicDatasourceHolder.setDataSource("site");
			SiteBaseSitecodeMapper siteBaseSitecodeMapper = applicationContext.getBean(SiteBaseSitecodeMapper.class);
			return siteBaseSitecodeMapper.selectOneByQuery(new QueryWrapper().eq("siteCode", siteCode));
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	public static SiteBaseCurrency getCurrencyByCurrency(String currencyCode) {
		try {
			DynamicDatasourceHolder.setDataSource("site");
			SiteBaseCurrencyMapper siteBaseCurrencyMapper = applicationContext.getBean(SiteBaseCurrencyMapper.class);
			return siteBaseCurrencyMapper.selectOneByQuery(new QueryWrapper().eq("currencyCode", currencyCode));
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	public static String getDate(String siteCode, Date date) {
		SiteBaseSitecode country = getSiteBySiteCode(siteCode);
		return new SimpleDateFormat(country.getDateFormat()).format(date);
	}

	public static String getDateTime(String siteCode, Date date) {
		SiteBaseSitecode country = getSiteBySiteCode(siteCode);
		return new SimpleDateFormat(country.getDateTimeFormat()).format(date);
	}

	public static String getDateTime(String siteCode, LocalDateTime date) {
		SiteBaseSitecode country = getSiteBySiteCode(siteCode);
		return DateTimeFormatter.ofPattern(country.getDateTimeFormat()).format(date);
	}

	/**
	 * 当前国家转换美元
	 * @param siteCode
	 * @param currencyCode
	 * @param price
	 * @return
	 */
	public static BigDecimal getUsdPrice(String siteCode, String currencyCode, BigDecimal price) {
		if(!currencyCode.equals("USD")){
			SiteBaseSitecode baseCountry = getSiteBySiteCode(siteCode);
			SiteBaseCurrency baseCountryCurrency = getCurrencyByCurrency(baseCountry.getCurrencyCode());
			price = price.multiply(baseCountryCurrency.getToUsdRate())
					.setScale(2, RoundingMode.HALF_UP);
		}
		return price;
	}

	/**
	 * 获取货币转换金额
	 * @param siteCode
	 * @param currencyCode
	 * @param price
	 * @return
	 */
	public static BigDecimal getPrice(String siteCode, String currencyCode, BigDecimal price) {
		SiteBaseSitecode baseCountry = getSiteBySiteCode(siteCode);
		SiteBaseCurrency baseCountryCurrency = getCurrencyByCurrency(baseCountry.getCurrencyCode());
		SiteBaseCurrency siteBaseCurrency = getCurrencyByCurrency(currencyCode);

		if(!baseCountry.getCountryCode().equals(currencyCode)){
			if(currencyCode.equals("USD")){
				//转换成美元
				price = price.multiply(baseCountryCurrency.getToUsdRate()).setScale(siteBaseCurrency.getDecimalNum(), RoundingMode.HALF_UP);
			}else{
				//转换成美元
				price = price.multiply(baseCountryCurrency.getToUsdRate());
				//转换成当前货币
				price = price.multiply(siteBaseCurrency.getFromUsdRate()).setScale(siteBaseCurrency.getDecimalNum(), RoundingMode.HALF_UP);
			}
			if(currencyCode.equals("VND")){
				//越南整数保留到千位
				price = price.divide(new BigDecimal(1000)).setScale(0, RoundingMode.HALF_UP).multiply(new BigDecimal(1000));
			}
		}
		return price;
	}

	/**
	 * 格式化成字符串
	 * @param siteCode
	 * @param currencyCode
	 * @param price
	 * @return
	 */
	public static String getPriceTxt(String siteCode, String currencyCode, BigDecimal price) {
		SiteBaseCurrency siteBaseCurrency = getCurrencyByCurrency(currencyCode);
        return siteBaseCurrency.getCurrencySymbol() + new DecimalFormat(siteBaseCurrency.getCurrencyFormat()).format(price);
	}

	/**
	 * 当前货币格式化
	 * @param currencyCode
	 * @param price
	 * @return
	 */
	public static String getCurrencyPriceTxt(String currencyCode, BigDecimal price) {
        return getPriceTxt("", currencyCode, price);
	}

	/**
	 * 支付类型格式化
	 * @param siteCode
	 * @param paymentType
	 * @return
	 */
	public static String getPaymentTypeTxt(String siteCode, int paymentType){
		try {
			DynamicDatasourceHolder.setDataSource("site");
			SiteBaseSitePaymentMapper siteBaseSitePaymentMapper = applicationContext.getBean(SiteBaseSitePaymentMapper.class);
			return siteBaseSitePaymentMapper.selectOneByQuery(new QueryWrapper().eq("siteCode", siteCode).eq("paymentId", paymentType)).getPaymentNameAlias();
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	/**
	 * 订单状态格式化
	 * @param siteCode
	 * @param orderStatus
	 * @return
	 */
	public static String getOrderStatusTxt(String siteCode, String orderStatus){
		try {
			SiteBaseLanguageOrderstatusMapper siteBaseLanguageOrderstatusMapper = applicationContext.getBean(SiteBaseLanguageOrderstatusMapper.class);
			SiteBaseSitecode baseCountry = getSiteBySiteCode(siteCode);
			DynamicDatasourceHolder.setDataSource("site");
			return siteBaseLanguageOrderstatusMapper.selectOneByQuery(new QueryWrapper().eq("languageCode",baseCountry.getLanguageCode()).eq("statusCode", orderStatus)).getStatusName();
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	/**
	 * 百分比
	 * @param price1
	 * @return
	 */
	public static String getPercentageTxt(BigDecimal price1){
		return "-"+price1.multiply(BigDecimal.valueOf(100L)).setScale(0,RoundingMode.HALF_EVEN).toString()+"%";
	}
}
