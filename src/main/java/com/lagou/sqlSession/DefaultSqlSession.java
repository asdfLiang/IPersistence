package com.lagou.sqlSession;

import com.lagou.domain.Configuration;
import com.lagou.domain.MappedStatement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author liangzj
 * @date 2021/1/24 23:21
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    SqlExecutor sqlExecutor = new SimpleSqlExecutor();

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> List<T> selectList(String statementId, Object... params) {
        DataSource dataSource = configuration.getDataSource();
        MappedStatement mappedStatement = configuration.getStatementMap().get(statementId);

        try {
            sqlExecutor.query(configuration, mappedStatement, params);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) {
        return null;
    }
}
