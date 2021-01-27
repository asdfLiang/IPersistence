package com.lagou.config;

import com.lagou.domain.Configuration;
import com.lagou.domain.MappedStatement;
import com.lagou.io.Resources;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 核心配置构建-MappedStatementMap构建类
 *
 * @author liangzj
 * @date 2021/1/24 20:57
 */
public class XmlMappedStatementMapBuilder {

    private Configuration configuration;

    public XmlMappedStatementMapBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(List<Element> mappersElements) throws DocumentException, ClassNotFoundException {

        Map<String, MappedStatement> mappedStatementMap = new HashMap<>();
        //
        for (Element mappersElement : mappersElements) {
            List<Element> mapperXmlElements = mappersElement.selectNodes("//mapper");
            // mapper
            for (Element mapperXmlElement : mapperXmlElements) {
                // 解析每个xxxMapper.xml文件
                List<MappedStatement> mappedStatements = parseEachXmlMappedStatement(mapperXmlElement);
                for (MappedStatement mappedStatement : mappedStatements) {
                    // 构建MappedStatement Map,key是statementId
                    mappedStatementMap.put(mappedStatement.getStatementId(), mappedStatement);
                }
            }
        }

        configuration.setStatementMap(mappedStatementMap);
    }

    /**
     * 解析一个xxxMapper.xml文件，将mapper.xml中的所有sql元素解析为MapStatement列表
     *
     * @param mapperElement
     * @return
     * @throws DocumentException
     */
    private List<MappedStatement> parseEachXmlMappedStatement(Element mapperElement) throws DocumentException, ClassNotFoundException {
        // resourcePath
        String resourcePath = mapperElement.attributeValue("resource");
        InputStream resourceAsStream = Resources.getResourceAsStream(resourcePath);
        // xxxMapper.xml
        Document mapperXmlDocument = new SAXReader().read(resourceAsStream);
        // <mapper>
        Element mapperRootElement = mapperXmlDocument.getRootElement();

        List<MappedStatement> mappedStatements = new LinkedList<>();
        // 构建select
        new XmlSqlMappedStatementBuilder(mappedStatements).buildSelect(mapperRootElement);
        // 构建insert
        new XmlSqlMappedStatementBuilder(mappedStatements).buildInsert(mapperRootElement);
        // 构建update
        new XmlSqlMappedStatementBuilder(mappedStatements).buildUpdate(mapperRootElement);
        // 构建delete
        new XmlSqlMappedStatementBuilder(mappedStatements).buildDelete(mapperRootElement);

        return mappedStatements;
    }

    /**
     * 核心配置构建-MappedStatement构建类
     *
     * @author liangzj
     * @date 2021/1/24 21:34
     */
    private class XmlSqlMappedStatementBuilder {

        private List<MappedStatement> mappedStatements;

        public XmlSqlMappedStatementBuilder(List<MappedStatement> mappedStatements) {
            this.mappedStatements = mappedStatements;
        }

        public void buildSelect(Element mapperRootElement) throws ClassNotFoundException {
            List<MappedStatement> selectMappedStatements = buildByElementName("//select", mapperRootElement);
            mappedStatements.addAll(selectMappedStatements);
        }

        public void buildInsert(Element mapperRootElement) throws ClassNotFoundException {
            List<MappedStatement> selectMappedStatements = buildByElementName("//insert", mapperRootElement);
            mappedStatements.addAll(selectMappedStatements);
        }

        public void buildUpdate(Element mapperRootElement) throws ClassNotFoundException {
            List<MappedStatement> selectMappedStatements = buildByElementName("//update", mapperRootElement);
            mappedStatements.addAll(selectMappedStatements);
        }

        public void buildDelete(Element mapperRootElement) throws ClassNotFoundException {
            List<MappedStatement> selectMappedStatements = buildByElementName("//delete", mapperRootElement);
            mappedStatements.addAll(selectMappedStatements);
        }

        /**
         * 根据标签名称构建MapStatement列表
         *
         * @param sqlElementName    sql标签名称
         * @param mapperRootElement xml文件的根节点
         * @return
         */
        private List<MappedStatement> buildByElementName(String sqlElementName, Element mapperRootElement) throws ClassNotFoundException {
            // xml文件获取
            String namespace = mapperRootElement.attributeValue("namespace");
            List<Element> selectElements = mapperRootElement.selectNodes(sqlElementName);

            List<MappedStatement> mappedStatements = new LinkedList<>();
            for (Element sqlElement : selectElements) {
                String id = sqlElement.attributeValue("id");
                String parameterTypeName = sqlElement.attributeValue("parameterType");
                String resultTypeName = sqlElement.attributeValue("resultType");
                String sql = sqlElement.getTextTrim();
                String statementId = namespace + "." + id;
                Class<?> parameterType = Class.forName(parameterTypeName);
                Class<?> resultType = Class.forName(resultTypeName);

                MappedStatement mappedStatement = new MappedStatement();
                mappedStatement.setStatementId(statementId);
                mappedStatement.setParameterType(parameterType);
                mappedStatement.setResultType(resultType);
                mappedStatement.setSql(sql);
                mappedStatements.add(mappedStatement);
            }

            return mappedStatements;
        }
    }
}