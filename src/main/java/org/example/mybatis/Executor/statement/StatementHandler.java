package org.example.mybatis.Executor.statement;

import org.example.mybatis.Executor.resultset.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface StatementHandler {
    /**
     * 准备语句
     */
    Statement prepare(Connection connection) throws SQLException;

    /** 参数化 */
    void parameterized(Statement statement) throws SQLException;

    /** 执行查询语句 */
    <E>List<E> query(Statement statement, ResultSetHandler resultSetHandler) throws SQLException;

}
