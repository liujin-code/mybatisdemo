package org.example.mybatis.session.defaults;

import org.example.mybatis.Executor.Executor;
import org.example.mybatis.mapping.Environment;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;
import org.example.mybatis.session.SqlSessionFactory;
import org.example.mybatis.session.TransactionIsolationLevel;
import org.example.mybatis.transaction.Transaction;
import org.example.mybatis.transaction.TransactionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        Environment environment = configuration.getEnvironment();
        TransactionFactory transactionFactory = environment.getTransactionFactory();
        Transaction tx = transactionFactory.newTransaction(environment.getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
        Executor executor = configuration.newExecutor(tx);
        return new DefaultSqlSession(configuration, executor);
    }
}
