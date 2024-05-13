package org.example.mybatis.session;

import lombok.Data;
import org.example.mybatis.Executor.Executor;
import org.example.mybatis.Executor.SimpleExecutor;
import org.example.mybatis.Executor.resultset.DefaultResultSetHandler;
import org.example.mybatis.Executor.resultset.ResultSetHandler;
import org.example.mybatis.Executor.statement.PreparedStatementHandler;
import org.example.mybatis.Executor.statement.StatementHandler;
import org.example.mybatis.binding.MapperRegistry;
import org.example.mybatis.datasource.druid.DruidDataSourceFactory;
import org.example.mybatis.datasource.pooled.PooledDataSourceFactory;
import org.example.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.Environment;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.reflection.factory.DefaultObjectFactory;
import org.example.mybatis.reflection.factory.ObjectFactory;
import org.example.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.example.mybatis.reflection.wrapper.ObjectWrapperFactory;
import org.example.mybatis.scripting.LanguageDriverRegistry;
import org.example.mybatis.scripting.xmltags.XMLLanguageDriver;
import org.example.mybatis.transaction.Transaction;
import org.example.mybatis.transaction.jdbc.JdbcTransactionFactory;
import org.example.mybatis.type.TypeAliasRegistry;
import org.example.mybatis.type.TypeHandlerRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class Configuration {
    //环境
    protected Environment environment;
    //映射注册机
    protected final MapperRegistry mapperRegistry = new MapperRegistry(this);
    //映射的语句
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();
    // 类型别名注册机
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
    // 脚本语言注册器
    protected final LanguageDriverRegistry languageDriverRegistry = new LanguageDriverRegistry();
    // 类型处理注册器
    protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
    // 对象工厂和对象包装器工厂
    protected ObjectFactory objectFactory = new DefaultObjectFactory();
    protected ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
    protected final Set<String> loadedResources = new HashSet<>();
    protected String databaseId;
    public Configuration() {
        typeAliasRegistry.registerAlias("jdbc", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("druid", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);

        languageDriverRegistry.setDefaultDriverClass(XMLLanguageDriver.class);
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

    public StatementHandler newStateMentHandler(SimpleExecutor simpleExecutor, MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(simpleExecutor, ms, parameter, resultHandler, boundSql);
    }

    public Executor newExecutor(Transaction transaction){
        return new SimpleExecutor(this, transaction);
    }
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor,mappedStatement,boundSql);
    }

    public MetaObject newMetaObject(Object parameterObject) {
        return MetaObject.forObject(parameterObject, objectFactory, objectWrapperFactory);
    }

    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }

    public LanguageDriverRegistry getLanguageRegistry() {
        return languageDriverRegistry;
    }
}
