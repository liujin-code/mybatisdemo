package org.example.mybatis.Executor.statement;

import org.example.mybatis.Executor.Executor;
import org.example.mybatis.Executor.resultset.ResultSetHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PrepareStatementHandler extends BaseStatementHandler {
    public PrepareStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultSetHandler resultSetHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, resultSetHandler, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = boundSql.getSql();
        return connection.prepareStatement(sql);
    }

    @Override
    public void parameterized(Statement statement) throws SQLException {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        preparedStatement.setLong(1, Long.parseLong(((Object[]) parameterObject)[0].toString()));
    }

    @Override
    public <E> List<E> query(Statement statement, ResultSetHandler resultSetHandler) throws SQLException {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        preparedStatement.execute();
        return resultSetHandler.handlerResultSet(preparedStatement);
    }
}
