package com.lagou.sqlSession;

import com.lagou.domain.Configuration;
import com.lagou.domain.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
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
        MappedStatement mappedStatement = configuration.getStatementMap().get(statementId);

        try {
            return sqlExecutor.query(configuration, mappedStatement, params);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) {

        List<Object> objects = selectList(statementId, params);
        if (objects == null || objects.size() == 0) {
            return null;
        }

        if (objects.size() != 1) {
            throw new IllegalStateException("result not only one");
        }

        return (T) objects.get(0);
    }
}
