package org.example.mybatis.session;

import org.example.mybatis.Executor.Executor;
import org.example.mybatis.Executor.SimpleExecutor;
import org.example.mybatis.Executor.resultset.DefaultResultSetHandler;
import org.example.mybatis.Executor.resultset.ResultSetHandler;
import org.example.mybatis.Executor.statement.PrepareStatementHandler;
import org.example.mybatis.Executor.statement.StatementHandler;
import org.example.mybatis.binding.MapperRegistry;
import org.example.mybatis.datasource.druid.DruidDataSourceFactory;
import org.example.mybatis.datasource.pooled.PooledDataSourceFactory;
import org.example.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.Environment;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.transaction.Transaction;
import org.example.mybatis.transaction.jdbc.JdbcTransactionFactory;
import org.example.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
    protected Environment environment;
    protected final MapperRegistry mapperRegistry = new MapperRegistry(this);

    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        typeAliasRegistry.registerAlias("jdbc", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("druid", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
    }

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public void addMappedStatement(MappedStatement mappedStatement) {
        mappedStatements.put(mappedStatement.getId(), mappedStatement);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public StatementHandler newStateMentHandler(SimpleExecutor simpleExecutor, MappedStatement ms, Object parameter, ResultSetHandler resultSetHandler, BoundSql boundSql) {
        return new PrepareStatementHandler(simpleExecutor, ms, parameter, resultSetHandler, boundSql);
    }

    public Executor newExecutor(Transaction transaction){
        return new SimpleExecutor(this, transaction);
    }
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor,mappedStatement,boundSql);
    }
}
