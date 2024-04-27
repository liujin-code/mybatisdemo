package org.example.mybatis.Executor;

import org.example.mybatis.Executor.resultset.ResultSetHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface Executor {
    ResultSetHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MappedStatement ms, Object parameter, ResultSetHandler resultSetHandler, BoundSql boundSql);

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceBack) throws SQLException;
}
