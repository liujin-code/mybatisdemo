package org.example.mybatis.type;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型处理器注册机
 */
public class TypeHandlerRegistry {

    private final Map<JdbcType, TypeHandler<?>> JDBC_TYPE_HANDLER_MAP = new EnumMap<>(JdbcType.class);

    private final Map<Type, Map<JdbcType, TypeHandler<?>>> TYPE_HANDLER_MAP = new HashMap<>();

    private final Map<Class<?>, TypeHandler<?>> ALL_TYPE_HANDLERS_MAP = new HashMap<>();

    public TypeHandlerRegistry() {

    }

    public void register(Type javaType, JdbcType jdbcType, TypeHandler<?> handler) {
        if (jdbcType != null) {
            TYPE_HANDLER_MAP.computeIfAbsent(javaType, k -> new HashMap<>()).put(jdbcType, handler);
        }
        ALL_TYPE_HANDLERS_MAP.put(handler.getClass(), handler);
    }
}
