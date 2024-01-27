package org.example.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapperRegistry {

    private Configuration configuration;

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    private final Map<Class<?>,MapperProxyFactory<?>> knownMappers = new HashMap<>();

    public <T> T getMapper(Class<T> type,SqlSession sqlSession){
        if (!hasMapper(type)){
            throw new RuntimeException("Type "+type+" is not known to the MapperRegistry.");
        }
        return (T) knownMappers.get(type).newInstance(sqlSession);
    }

    public <T> void addMapper(Class<T> type){
        if (type.isInterface()){
            if (hasMapper(type)){
                throw new RuntimeException("Type "+type+" is already known to the MapperRegistry.");
            }
            knownMappers.put(type,new MapperProxyFactory<>(type));
        }
    }

    public void addMappers(String packageName){
        // 扫描包下所有的方法
        Set<Class<?>> classes = ClassScanner.scanPackage(packageName);
        for (Class<?> aClass : classes) {
            addMapper(aClass);
        }
    }
    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

}
