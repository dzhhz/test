package com.feilu.api.dao;

import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * mybatis-flex 代码生成
 * @author dzh
 */
public class Codegen {

    public static void main(String[] args) {

        systemGenerator();
        siteGenerator();
    }

    public static void systemGenerator() {
        //配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://106.54.174.9/feilu_system?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull");
        dataSource.setUsername("fldev");
        dataSource.setPassword("Fei5151309");
        //创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        //设置根包
        globalConfig.setBasePackage("com.feilu.api.dao.system");
        globalConfig.setMapperXmlPath("src/main/resources/mapper/system");

        //设置表前缀和只生成哪些表
        globalConfig.setTablePrefix("tb_");
        globalConfig.setGenerateTable("tb_category", "tb_category_attribute", "tb_category_attribute_alias",
                "tb_category_attribute_value_alias", "tb_category_attribute_value", "tb_member_feedback", "tb_member_wallet",
                "tb_member_wallet_record", "tb_order", "tb_item", "tb_item_ranking", "tb_product", "tb_order_aftersale", "tb_order_consignee",
                "tb_order_items", "tb_product_alias", "tb_item_sku", "tb_order_aftersale_status_alias", "tb_order_aftersale_status",
                "tb_order_ext", "tb_order_tracking_record", "tb_order_blacklist", "tb_order_tracking_status",
                "tb_order_tracking_status_alias", "tb_order_aftersale_reason", "tb_order_aftersale_reason_alias", "tb_log_order",
                "tb_order_items_backup", "tb_member_whatsapp_business");

        //设置生成 entity 并启用 Lombok
        globalConfig.setEntityGenerateEnable(true);
        globalConfig.setEntityWithLombok(true);
        globalConfig.setEntityClassPrefix("System");
        //设置项目的JDK版本，项目的JDK为14及以上时建议设置该项，小于14则可以不设置
        globalConfig.setEntityJdkVersion(17);

        //设置生成 mapper
        globalConfig.setMapperGenerateEnable(true);
        globalConfig.setMapperClassPrefix("System");
        globalConfig.setMapperAnnotation(true);

        globalConfig.getMapperXmlConfig()
                .setFilePrefix("System")
                .setFileSuffix("Mapper");
        globalConfig.setMapperXmlGenerateEnable(true);

        //通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);
        //生成代码
        generator.generate();
    }

    public static void siteGenerator() {
        //配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://106.54.174.9/feilu_website?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull");
        dataSource.setUsername("fldev");
        dataSource.setPassword("Fei5151309");
        //创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        //设置根包
        globalConfig.setBasePackage("com.feilu.api.dao.website");
        globalConfig.setMapperXmlPath("src/main/resources/mapper/website");

        //设置表前缀和只生成哪些表
        globalConfig.setTablePrefix("site_");
        globalConfig.setGenerateTable("site_base_sitecode", "site_base_currency", "site_base_site_payment", "site_base_language_orderstatus", "site_order",
                "site_order_items", "site_item_sku", "site_item", "site_order_consignee", "site_order_bill", "site_promotion_coupon_record",
                "site_area_th", "site_area_gr");

        //设置生成 entity 并启用 Lombok
        globalConfig.setEntityGenerateEnable(true);
        globalConfig.setEntityWithLombok(true);
        globalConfig.setEntityClassPrefix("Site");
        //设置项目的JDK版本，项目的JDK为14及以上时建议设置该项，小于14则可以不设置
        globalConfig.setEntityJdkVersion(17);

        //设置生成 mapper
        globalConfig.setMapperGenerateEnable(true);
        globalConfig.setMapperClassPrefix("Site");
        globalConfig.setMapperAnnotation(true);

        globalConfig.getMapperXmlConfig()
                .setFilePrefix("Site")
                .setFileSuffix("Mapper");
        globalConfig.setMapperXmlGenerateEnable(true);

        //通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);
        //生成代码
        generator.generate();
    }
}
