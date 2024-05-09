package org.example.mybatis.mapping;

public interface SqlSource {
    BoundSql getBoundSql(Object parameterObject);
}
