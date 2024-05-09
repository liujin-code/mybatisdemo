package org.example.mybatis.builder;


import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.ParameterMapping;
import org.example.mybatis.mapping.SqlSource;
import org.example.mybatis.session.Configuration;

import java.util.List;

/**
 * @author 小傅哥，微信：fustack
 * @description 静态SQL源码
 * @date 2022/5/17
 * @github https://github.com/fuzhengwei/CodeDesignTutorials
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class StaticSqlSource implements SqlSource {

    private final String sql;
    private final List<ParameterMapping> parameterMappings;
    private final Configuration configuration;

    public StaticSqlSource(Configuration configuration, String sql) {
        this(configuration, sql, null);
    }

    public StaticSqlSource(Configuration configuration, String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(configuration, sql, parameterMappings, parameterObject);
    }

}
