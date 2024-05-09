package org.example.mybatis.scripting;

import org.dom4j.Element;
import org.example.mybatis.mapping.SqlSource;
import org.example.mybatis.session.Configuration;

/**
 * 脚本语言驱动
 */
public interface LanguageDriver {
    SqlSource createSqlSource(Configuration configuration, Element element, Class<?> parameterTypeClass);
}
