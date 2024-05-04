package org.example.mybatis.reflection.wrapper;

import org.example.mybatis.reflection.MetaObject;

public interface ObjectWrapperFactory {
    /**
     * 判断有没有包装器
     * @param object
     * @return
     */
    boolean hasWrapperFor(Object object);

    /**
     * 获取包装器
     * @param metaObject
     * @param object
     * @return
     */
    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);
}
