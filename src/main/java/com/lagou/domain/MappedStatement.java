package com.lagou.domain;

import com.lagou.enums.SqlCommandType;

/**
 * @author liangzj
 * @date 2021/1/24 19:39
 */
public class MappedStatement {

    private String statementId;

    private Class<?> resultType;

    private Class<?> parameterType;

    private String sql;

    private SqlCommandType commandType;

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public SqlCommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(SqlCommandType commandType) {
        this.commandType = commandType;
    }
}
