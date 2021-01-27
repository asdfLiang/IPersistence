package com.lagou.domain;

import com.lagou.utils.ParameterMapping;

import java.util.List;

/**
 * @author liangzj
 * @date 2021/1/24 23:55
 */
public class BoundSql {

    private String sqlText;

    List<ParameterMapping> parameterMappings;

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }
}
