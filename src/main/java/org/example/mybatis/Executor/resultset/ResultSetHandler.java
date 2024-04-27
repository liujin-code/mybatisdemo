package org.example.mybatis.Executor.resultset;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface ResultSetHandler {
    <E> List<E> handlerResultSet(Statement stmt) throws SQLException;

}
