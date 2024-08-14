package com.feilu.api.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
@Component
public class TemplateService {


    private Configuration freemarkerConfig;

    @Autowired
    public void setFreemarkerConfig(Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }

    public String renderDynamicTemplate(String templateContent, Map<String, Object> map) {

        try {
            // 创建 FreeMarker 模板
            Template template = new Template("dynamicTemplate", new StringReader(templateContent), freemarkerConfig);

            // 将 map 转换为 ModelMap
            ModelMap modelMap = new ModelMap();
            modelMap.addAllAttributes(map);

            // 渲染模板
            StringWriter writer = new StringWriter();
            template.process(modelMap, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("renderDynamicTemplate error", e);
            return "";
        }
    }
}
