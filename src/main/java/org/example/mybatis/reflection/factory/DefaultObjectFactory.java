package org.example.mybatis.reflection.factory;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

public class DefaultObjectFactory implements ObjectFactory, Serializable {
    private static final long serialVersionUID = -8855120656740914948L;

    @Override
    public void setProperties(Properties properties) {
        // no props for default 默认无属性可设置
    }

    @Override
    public <T> T create(Class<T> type) {
        return create(type, null, null);
    }

    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgsTypes, List<Object> constructorArgs) {
        Class<?> aClass = resolveInterface(type);
        return (T) instantiateClass(aClass, constructorArgsTypes, constructorArgs);
    }

    @Override
    public <T> boolean isCollection(Class<T> type) {
        return Collection.class.isAssignableFrom(type);
    }

    /**
     * 获取默认的集合实现类
     *
     * @param type
     * @return
     */
    public Class<?> resolveInterface(Class<?> type) {
        Class<?> classToCreate;
        if (type == List.class || type == Collection.class || type == Iterable.class) {
            classToCreate = ArrayList.class;
        } else if (type == Set.class) {
            classToCreate = HashSet.class;
        } else if (type == SortedSet.class) {
            classToCreate = TreeSet.class;
        } else if (type == Map.class) {
            classToCreate = HashMap.class;
        } else {
            classToCreate = type;
        }
        return classToCreate;
    }

    private <T> T instantiateClass(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        Constructor<T> constructor;
        try {
            if (constructorArgTypes == null || constructorArgs == null) {
                Constructor<T> declaredConstructor = type.getDeclaredConstructor();
                if (!declaredConstructor.isAccessible()) {
                    declaredConstructor.setAccessible(true);
                }
                return declaredConstructor.newInstance();
            }
            constructor = type.getDeclaredConstructor(constructorArgTypes.toArray(new Class[constructorArgTypes.size()]));
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(constructorArgs.toArray(new Object[constructorArgs.size()]));
        } catch (Exception e) {
            StringBuilder argTypes = new StringBuilder();
            if (constructorArgTypes != null) {
                for (Class<?> constructorArgType : constructorArgTypes) {
                    argTypes.append(constructorArgType.getSimpleName()).append(",");
                }
            }
            StringBuilder argValues = new StringBuilder();
            if (constructorArgs != null){
                for (Object argValue : constructorArgs) {
                    argValues.append(argValue).append(",");
                }
            }
            throw new RuntimeException("Error instantiating " + type + " with invalid types (" + argTypes + ") or values (" + argValues + "). Cause: " + e, e);
        }
    }
}
