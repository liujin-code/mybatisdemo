package org.example.mybatis.reflection.factory;

import java.util.List;
import java.util.Properties;

/**
 * @description: 对象工厂接口
 */
public interface ObjectFactory {

    /**
     * 设置属性
     * @param properties
     */
    void setProperties(Properties properties);

    /**
     * 创建对象
     * @param type 类型
     * @return 对象
     * @param <T> 泛型
     */
    <T>T create(Class<T> type);

    /**
     * 创建对象 利用指定构造参数类型和构造参数
     * @param type 类型
     * @param constructorArgsTypes 构造函数参数类型
     * @param constructorArgs 构造函数参数
     * @param <T> 泛型
     * @return 对象
     */
    <T>T create(Class<T> type, List<Class<?>> constructorArgsTypes, List<Object> constructorArgs);

    /**
     * 是否是集合
     * @param type 类型
     * @param <T> 泛型
     * @return 是否是集合
     */
    <T> boolean isCollection(Class<T> type);
}
