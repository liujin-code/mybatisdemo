package org.example.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface TypeHandler<T> {

    /**
     * 设置参数
     */
    void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;
}
