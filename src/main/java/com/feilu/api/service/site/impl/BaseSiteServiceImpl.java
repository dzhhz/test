package com.feilu.api.service.site.impl;


import com.feilu.api.common.config.DynamicDatasourceHolder;
import com.feilu.api.dao.website.entity.SiteBaseCurrency;
import com.feilu.api.dao.website.entity.SiteBaseSitecode;
import com.feilu.api.dao.website.mapper.SiteBaseCurrencyMapper;
import com.feilu.api.dao.website.mapper.SiteBaseSitecodeMapper;
import com.feilu.api.service.site.BaseSiteService;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dzh
 */
@Service
public class BaseSiteServiceImpl implements BaseSiteService {

    private SiteBaseSitecodeMapper siteBaseSitecodeMapper;
	private SiteBaseCurrencyMapper siteBaseCurrencyMapper;

	@Autowired
	public void setSiteBaseSitecodeMapper(SiteBaseSitecodeMapper siteBaseSitecodeMapper) {
		this.siteBaseSitecodeMapper = siteBaseSitecodeMapper;
	}

	@Autowired
	public void setSiteBaseCurrencyMapper(SiteBaseCurrencyMapper siteBaseCurrencyMapper) {
		this.siteBaseCurrencyMapper = siteBaseCurrencyMapper;
	}

	@Override
	public SiteBaseSitecode getBaseSite(String siteCode) {
		try {
			DynamicDatasourceHolder.setDataSource("site");
			return siteBaseSitecodeMapper.selectOneByQuery(new QueryWrapper().eq("siteCode", siteCode));
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

	@Override
	public SiteBaseCurrency getBaseCurrency(String currencyCode) {
		try {
			DynamicDatasourceHolder.setDataSource("site");
			return siteBaseCurrencyMapper.selectOneByQuery(new QueryWrapper().eq("currencyCode", currencyCode));
		} finally {
			DynamicDatasourceHolder.removeDataSource();
		}
	}

}
