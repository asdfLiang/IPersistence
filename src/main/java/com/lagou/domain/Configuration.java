package com.lagou.domain;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author liangzj
 * @date 2021/1/24 19:39
 */
public class Configuration {

    private DataSource dataSource;

    private Map<String, MappedStatement> statementMap;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getStatementMap() {
        return statementMap;
    }

    public void setStatementMap(Map<String, MappedStatement> statementMap) {
        this.statementMap = statementMap;
    }
}
