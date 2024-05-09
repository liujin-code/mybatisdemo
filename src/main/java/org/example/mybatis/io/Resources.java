package org.example.mybatis.io;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Resources {
    public static Reader getResourceAsReader(String resource) {
        return new InputStreamReader(getResourceAsStream(resource));
    }

    public static InputStream getResourceAsStream(String resource) {
        ClassLoader[] classLoaders = getClassLoaders();
        for (ClassLoader classLoader : classLoaders) {
            InputStream resourceAsStream = classLoader.getResourceAsStream(resource);
            if (resourceAsStream != null) {
                return resourceAsStream;
            }
        }
        return null;
    }
    public static ClassLoader[] getClassLoaders() {
        return new ClassLoader[] {
                Thread.currentThread().getContextClassLoader(),
                ClassLoader.getSystemClassLoader()
        };
    }

    public static Class<?> classForName(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }
}
