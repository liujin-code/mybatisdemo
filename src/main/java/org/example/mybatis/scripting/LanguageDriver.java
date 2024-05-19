package org.example.mybatis.scripting;

import org.dom4j.Element;
import org.example.mybatis.Executor.parameter.ParameterHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.mapping.SqlSource;
import org.example.mybatis.session.Configuration;

/**
 * 脚本语言驱动
 */
public interface LanguageDriver {
    /**
     * 创建SQL源码(mapper xml方式)
     */
    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);

    /**
     * 创建参数处理器
     */
    ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);
}
