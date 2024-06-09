package org.example.mybatis.Executor.keygen;

import org.example.mybatis.Executor.Executor;
import org.example.mybatis.mapping.MappedStatement;

import java.sql.Statement;

public class NoKeyGenerator implements KeyGenerator{
    @Override
    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        // Do Nothing
    }

    @Override
    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        // Do Nothing
    }
}
