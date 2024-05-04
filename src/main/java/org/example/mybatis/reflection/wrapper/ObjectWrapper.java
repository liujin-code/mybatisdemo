package org.example.mybatis.reflection.wrapper;

import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.reflection.property.PropertyTokenizer;

import java.util.List;

public interface ObjectWrapper {
    /**
     * get
     */
    Object get(PropertyTokenizer propertyTokenizer);

    /**
     * set
     */
    void set(PropertyTokenizer propertyTokenizer,Object value);

    /**
     * 查找属性
     */
    String findProperty(String name,boolean useCamelCaseMapping);

    /**
     * 取得getter的名字列表
     */
    String[] getGetterNames();

    /**
     * 获得Setter的名字列表
     */
    String[] getSetterNames();

    /**
     * 取得getter的类型
     */
    Class<?> getGetterType(String name);

    /**
     * 获得setter的类型
     */
    Class<?> getSetterType(String name);

    /**
     * 是否有指定的setter
     */
    boolean hasSetter(String name);

    /**
     * 是否有指定的getter
     */
    boolean hasGetter(String name);

    /**
     * 实例化属性
     */
    MetaObject instantiatePropertyValue(String name, PropertyTokenizer propertyTokenizer, org.example.mybatis.reflection.factory.ObjectFactory objectFactory);

    /**
     * 是否是集合
     */
    boolean isCollection();

    /**
     * 添加属性
     */
    void add(Object element);

    /**
     * 添加属性
     */
    <E> void addAll(List<E> element);
}
