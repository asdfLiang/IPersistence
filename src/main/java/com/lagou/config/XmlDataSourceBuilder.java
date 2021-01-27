package com.lagou.config;

import com.lagou.domain.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Element;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.Properties;

/**
 * 核心配置构建-数据源构建类
 *
 * @author liangzj
 * @date 2021/1/24 21:02
 */
public class XmlDataSourceBuilder {

    private Configuration configuration;

    public XmlDataSourceBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 根据配置文件构建数据源
     *
     * @param dataSourceElement <dataSource>
     * @return
     */
    public void parse(Element dataSourceElement) throws PropertyVetoException {

        List<Element> dataSourcePropertyList = dataSourceElement.selectNodes("//property");

        Properties properties = new Properties();
        for (Element dataSourceProperty : dataSourcePropertyList) {
            String name = dataSourceProperty.attributeValue("name");
            String value = dataSourceProperty.attributeValue("value");
            properties.setProperty(name, value);
        }

        DataSource dataSource = buildDataSource(properties);

        configuration.setDataSource(dataSource);
    }

    private DataSource buildDataSource(Properties properties) throws PropertyVetoException {
        String driverClassName = properties.getProperty("driverClassName");
        String jdbcUrl = properties.getProperty("jdbcUrl");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(driverClassName);
        comboPooledDataSource.setJdbcUrl(jdbcUrl);
        comboPooledDataSource.setUser(username);
        comboPooledDataSource.setPassword(password);

        return comboPooledDataSource;
    }

}
