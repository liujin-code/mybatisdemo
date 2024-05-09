package org.example.mybatis.mapping;

import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.session.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundSql {

    private final String sql;
    private final List<ParameterMapping> parameterMappings;
    private final Object parameterObject;
    private final Map<String, Object> additionalParameters;
    private final MetaObject metaParameters;

    public BoundSql(Configuration configuration, String sql, List<ParameterMapping> parameterMappings, Object parameterObject) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.parameterObject = parameterObject;
        this.additionalParameters = new HashMap<>();
        this.metaParameters = configuration.newMetaObject(additionalParameters);
    }

    public String getSql() {
        return sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public boolean hasAdditionalParameter(String name) {
        return metaParameters.hasGetter(name);
    }

    public void setAdditionalParameter(String name, Object value) {
        metaParameters.setValue(name, value);
    }

    public Object getAdditionalParameter(String name) {
        return metaParameters.getValue(name);
    }

}
