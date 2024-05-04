package org.example.mybatis.reflection;

import org.example.mybatis.reflection.factory.DefaultObjectFactory;
import org.example.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.example.mybatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * 系统级别元对象
 */
public class SystemMetaObject {
    public static final org.example.mybatis.reflection.factory.ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();

    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER = new DefaultObjectWrapperFactory();

    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER);

    public SystemMetaObject() {
    }

    /**
     * 空对象
     */
    private static class NullObject {
    }

    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER);
    }
}
