package org.example.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeHandler<T> {

    /**
     * 设置参数
     */
    void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

    T getResult(ResultSet resultSet, String columnName) throws SQLException;

    /**
     * 取得结果
     */
    T getResult(ResultSet rs, int columnIndex) throws SQLException;
}
