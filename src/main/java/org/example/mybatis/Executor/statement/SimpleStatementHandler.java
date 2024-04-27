package org.example.mybatis.Executor.statement;

import org.example.mybatis.Executor.Executor;
import org.example.mybatis.Executor.resultset.ResultSetHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleStatementHandler extends BaseStatementHandler{
    public SimpleStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultSetHandler resultSetHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, resultSetHandler, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

    @Override
    public void parameterized(Statement statement) throws SQLException {

    }

    @Override
    public <E> List<E> query(Statement statement, ResultSetHandler resultSetHandler) throws SQLException {
        String sql = boundSql.getSql();
        statement.execute(sql);
        return resultSetHandler.handlerResultSet(statement);
    }
}
