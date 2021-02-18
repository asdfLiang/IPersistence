package com.lagou.sqlSession;

import com.lagou.domain.Configuration;
import com.lagou.domain.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.*;
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
        } catch (SQLException throwable) {
            throwable.printStackTrace();
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

    @Override
    public Integer insert(String statementId, Object... params) {
        return update(statementId, params);
    }

    @Override
    public Integer update(String statementId, Object... params) {
        MappedStatement mappedStatement = configuration.getStatementMap().get(statementId);
        try {
            return sqlExecutor.update(configuration, mappedStatement, params);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
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
    public Integer delete(String statementId, Object... params) {
        return update(statementId, params);
    }

    @Override
    public <T> T getMapper(Class<T> clazz) {

        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementId = className + "." + methodName;

                MappedStatement mappedStatement = configuration.getStatementMap().get(statementId);

                switch (mappedStatement.getCommandType()) {
                    case SELECT: {
                        Type genericReturnType = method.getGenericReturnType();
                        if (genericReturnType instanceof ParameterizedType) {
                            return selectList(statementId, args);
                        }
                        return selectOne(statementId, args);
                    }
                    case INSERT:
                        return insert(statementId, args);
                    case UPDATE:
                        return update(statementId, args);
                    case DELETE:
                        return delete(statementId, args);
                    default:
                        throw new IllegalArgumentException("unknown command type");
                }
            }
        });

        return (T) proxyInstance;
    }
}
