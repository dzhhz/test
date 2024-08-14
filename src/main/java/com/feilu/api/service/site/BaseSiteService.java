package com.feilu.api.service.site;

import com.feilu.api.dao.website.entity.SiteBaseCurrency;
import com.feilu.api.dao.website.entity.SiteBaseSitecode;

public interface BaseSiteService {

    /**
     * 获取站点信息
     * @param siteCode 站点
     * @return 信息
     */
    SiteBaseSitecode getBaseSite(String siteCode);

    /**
     * 获取货币信息
     * @param currencyCode 货币代码
     * @return 信息
     */
    SiteBaseCurrency getBaseCurrency(String currencyCode);
}
