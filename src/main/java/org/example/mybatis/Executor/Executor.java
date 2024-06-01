package org.example.mybatis.Executor;

import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.ResultHandler;
import org.example.mybatis.session.RowBounds;
import org.example.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface Executor {
    ResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException;

    int update(MappedStatement ms, Object parameter) throws SQLException;

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceBack) throws SQLException;
}
