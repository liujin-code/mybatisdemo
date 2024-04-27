package org.example.mybatis.Executor;

import org.example.mybatis.Executor.resultset.ResultSetHandler;
import org.example.mybatis.Executor.statement.StatementHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleExecutor extends BaseExecutor {
    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultSetHandler resultSetHandler, BoundSql boundSql) throws SQLException {
        Configuration configuration = ms.getConfiguration();
        StatementHandler handler = configuration.newStateMentHandler(this, ms, parameter, resultSetHandler, boundSql);
        Connection connection = transaction.getConnection();
        Statement statement = handler.prepare(connection);
        return handler.query(statement, resultSetHandler);
    }
}
