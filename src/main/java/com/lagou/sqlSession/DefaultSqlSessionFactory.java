package com.lagou.sqlSession;

import com.lagou.domain.Configuration;

/**
 * 绘画工厂
 *
 * @author liangzj
 * @date 2021/1/24 23:16
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSqlSession() {
        return new DefaultSqlSession(configuration);
    }
}
