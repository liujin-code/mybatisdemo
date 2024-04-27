package org.example.mybatis.Executor.statement;

import org.example.mybatis.Executor.Executor;
import org.example.mybatis.Executor.resultset.ResultSetHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.Configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseStatementHandler implements StatementHandler {

    protected final Configuration configuration;
    protected final Executor executor;
    protected final MappedStatement mappedStatement;

    protected final Object parameterObject;

    protected final ResultSetHandler resultSetHandler;

    protected BoundSql boundSql;

    public BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultSetHandler resultSetHandler, BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.parameterObject = parameterObject;
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, boundSql);
        this.boundSql = boundSql;
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        try {
            Statement statement = instantiateStatement(connection);
            statement.setQueryTimeout(300);
            statement.setFetchSize(10000);
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
        }
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;

}
