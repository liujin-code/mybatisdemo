package org.example.mybatis.session;

import java.sql.SQLException;

public interface SqlSession {

    <T> T selectOne(String statement, Object parameter) throws SQLException;

    <T> T selectOne(String statement);

    <T> T getMapper(Class<T> type);

    Configuration getConfiguration();
}
