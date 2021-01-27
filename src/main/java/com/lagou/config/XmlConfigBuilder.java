package com.lagou.config;

import com.lagou.domain.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;

/**
 * 核心配置文件构建类
 *
 * @author liangzj
 * @date 2021/1/24 19:47
 */
public class XmlConfigBuilder {

    /**
     * 使用dom4j解析配置文件，构建核心配置文件
     *
     * @param in
     * @return
     */
    public Configuration parseConfig(InputStream in) throws DocumentException, PropertyVetoException, ClassNotFoundException {

        Document sqlMapConfigDocument = new SAXReader().read(in);
        // <configuration>
        Element rootElement = sqlMapConfigDocument.getRootElement();
        // <dataSource>
        List<Element> dataSourceElements = rootElement.selectNodes("//dataSource");
        // <mappers>
        List<Element> mappersElements = rootElement.selectNodes("//mappers");

        // 配置构建
        Configuration configuration = new Configuration();
        // TODO 解析数据源, 此处不考虑多数据源
        new XmlDataSourceBuilder(configuration).parse(dataSourceElements.get(0));
        // 构建mappedStatement
        new XmlMappedStatementMapBuilder(configuration).parse(mappersElements);

        return configuration;
    }

}
