package org.example.mybatis.reflection;

import lombok.Data;
import org.example.mybatis.reflection.factory.ObjectFactory;
import org.example.mybatis.reflection.property.PropertyTokenizer;
import org.example.mybatis.reflection.wrapper.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 元对象
 */
@Data
public class MetaObject {
    /**
     * 源对象
     */
    private Object originObject;

    /**
     * 对象包装器
     */
    private ObjectWrapper objectWrapper;

    /**
     * 对象工厂
     */
    private ObjectFactory objectFactory;

    /**
     * 对象包装工厂
     */
    private ObjectWrapperFactory objectWrapperFactory;

    public MetaObject(Object object, org.example.mybatis.reflection.factory.ObjectFactory defaultObjectFactory, ObjectWrapperFactory defaultObjectWrapper) {
        this.originObject = object;
        this.objectFactory = defaultObjectFactory;
        this.objectWrapperFactory = defaultObjectWrapper;
        if (object instanceof ObjectWrapper) {
            // 如果object就是包装类型
            this.objectWrapper = (ObjectWrapper) object;
        } else if (objectWrapperFactory.hasWrapperFor(object)) {
            this.objectWrapper = objectWrapperFactory.getWrapperFor(this, object);
        } else if (object instanceof Map) {
            this.objectWrapper = new MapWrapper(this, (Map) object);
        } else if (object instanceof Collection) {
            this.objectWrapper = new CollectionWrapper(this, (Collection) object);
        } else {
            this.objectWrapper = new BeanWrapper(this, object);
        }
    }


    public static MetaObject forObject(Object object, ObjectFactory defaultObjectFactory, ObjectWrapperFactory defaultObjectWrapper) {
        if (object == null) {
            // 处理一下null,将null包装起来
            return SystemMetaObject.NULL_META_OBJECT;
        } else {
            return new MetaObject(object, defaultObjectFactory, defaultObjectWrapper);
        }
    }

    public Object getValue(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaObject = metaObjectForProperty(prop.getIndexedName());
            if (metaObject == SystemMetaObject.NULL_META_OBJECT) {
                // 如果上层是null直接返回null
                return null;
            } else {
                // 否则获取下层的值
                return metaObject.getValue(prop.getChildren());
            }
        } else {
            // 最后一层 获取值
            return objectWrapper.get(prop);
        }
    }

    public void setValue(String name, Object value) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaObject = metaObjectForProperty(prop.getIndexedName());
            if (metaObject == SystemMetaObject.NULL_META_OBJECT) {
                if (value == null && prop.getChildren() != null) {
                    return;
                } else {
                    metaObject = objectWrapper.instantiatePropertyValue(name, prop, objectFactory);
                }
            }
            // 递归设置最下层的值
            metaObject.setValue(prop.getChildren(), value);
        } else {
            // 最后一层 获取值
            objectWrapper.set(prop, value);
        }
    }

    public MetaObject metaObjectForProperty(String indexedName) {
        Object value = getValue(indexedName);
        return MetaObject.forObject(value, objectFactory, objectWrapperFactory);
    }

    /**
     * get
     */
    public Object get(PropertyTokenizer propertyTokenizer) {
        return objectWrapper.get(propertyTokenizer);
    }

    /**
     * set
     */
    public void set(PropertyTokenizer propertyTokenizer, Object value) {
        objectWrapper.set(propertyTokenizer, value);
    }

    /**
     * 查找属性
     */
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return objectWrapper.findProperty(name, useCamelCaseMapping);
    }

    /**
     * 取得getter的名字列表
     */
    public String[] getGetterNames() {
        return objectWrapper.getGetterNames();
    }

    /**
     * 获得Setter的名字列表
     */
    public String[] getSetterNames() {
        return objectWrapper.getSetterNames();
    }

    /**
     * 取得getter的类型
     */
    public Class<?> getGetterType(String name) {
        return objectWrapper.getGetterType(name);
    }

    /**
     * 获得setter的类型
     */
    public Class<?> getSetterType(String name) {
        return objectWrapper.getSetterType(name);
    }

    /**
     * 是否有指定的setter
     */
    public boolean hasSetter(String name) {
        return objectWrapper.hasSetter(name);
    }

    /**
     * 是否有指定的getter
     */
    public boolean hasGetter(String name) {
        return objectWrapper.hasGetter(name);
    }


    /**
     * 是否是集合
     */
    public boolean isCollection() {
        return objectWrapper.isCollection();
    }

    /**
     * 添加属性
     */
    public void add(Object element) {
        objectWrapper.add(element);
    }

    /**
     * 添加属性
     */
    public <E> void addAll(List<E> element) {
        objectWrapper.addAll(element);
    }

    public Object getOriginalObject() {
        return originObject;
    }
}
