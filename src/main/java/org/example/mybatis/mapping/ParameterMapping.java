package org.example.mybatis.mapping;

import lombok.Builder;
import lombok.Data;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.type.JdbcType;

@Builder
@Data
public class ParameterMapping {
    private Configuration configuration;
    // property
    private String property;
    // javaType = int
    private Class<?> javaType = Object.class;
    // jdbcType=NUMERIC
    private JdbcType jdbcType;
}
