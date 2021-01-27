package com.lagou.sqlSession;

import com.lagou.domain.BoundSql;
import com.lagou.domain.Configuration;
import com.lagou.domain.MappedStatement;
import com.lagou.utils.GenericTokenParser;
import com.lagou.utils.ParameterMapping;
import com.lagou.utils.ParameterMappingTokenHandler;

import javax.sql.DataSource;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author liangzj
 * @date 2021/1/24 23:33
 */
public class SimpleSqlExecutor implements SqlExecutor {

    @Override
    public <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object param)
            throws SQLException, NoSuchFieldException, IllegalAccessException, InstantiationException,
            IntrospectionException, InvocationTargetException {
        DataSource dataSource = configuration.getDataSource();
        // 获取连接
        Connection connection = dataSource.getConnection();
        // 获取要执行的sql
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);

        // 获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        // 设置参数
        setSqlParam(preparedStatement, param, boundSql.getParameterMappings());

        // 执行sql
        ResultSet resultSet = preparedStatement.executeQuery();

        // 转换结果并返回
        return transferResult(resultSet, mappedStatement.getResultType());

    }

    private <T> List<T> transferResult(ResultSet resultSet, Class<?> resultTypeClass)
            throws SQLException, IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException {

        LinkedList<T> results = new LinkedList<>();
        while (resultSet.next()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            T t = (T) resultTypeClass.newInstance();
            for (int i = 0; i < columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(columnName);

                // 获取set方法名称，并执行写入
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(t, columnValue);
            }

            results.add(t);
        }

        return null;
    }

    private void setSqlParam(PreparedStatement preparedStatement, Object param, List<ParameterMapping> parameterMappings)
            throws NoSuchFieldException, IllegalAccessException, SQLException {

        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String fieldName = parameterMapping.getContent();
            Object fieldValue = getFieldValue(param, fieldName);

            preparedStatement.setObject(i + 1, fieldValue);
        }

    }

    /**
     * 按照变量名称获取对象中的名称
     *
     * @param object    目标对象
     * @param fieldName 要获取的字段名称
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field declaredField = clazz.getDeclaredField(fieldName);
        boolean accessible = declaredField.isAccessible();
        declaredField.setAccessible(true);

        Object fieldValue = declaredField.get(object);

        declaredField.setAccessible(accessible);

        return fieldValue;
    }

    /**
     * 完成对#{}的解析，1. 将#{}用?替换 2. 解析出#{}里面的值进行存储
     *
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        //
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String sqlText = genericTokenParser.parse(sql);

        BoundSql boundSql = new BoundSql();
        boundSql.setSqlText(sqlText);
        boundSql.setParameterMappings(parameterMappingTokenHandler.getParameterMappings());

        return boundSql;
    }


}
