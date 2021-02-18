package com.lagou.sqlSession;

import com.lagou.domain.Configuration;
import com.lagou.domain.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author liangzj
 * @date 2021/1/24 23:33
 */
public interface SqlExecutor {

    <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object... param) throws SQLException, NoSuchFieldException, IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException;

    Integer update(Configuration configuration, MappedStatement mappedStatement, Object... param) throws SQLException, NoSuchFieldException, IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException;

}
