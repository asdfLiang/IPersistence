package com.lagou.sqlSession;

import java.util.List;

/**
 * @author liangzj
 * @date 2021/1/24 19:45
 */
public interface SqlSession {

    <T> List<T> selectList(String statementId, Object... params);

    <T> T selectOne(String statementId, Object... params);

    <T> T getMapper(Class<T> clazz);
}
