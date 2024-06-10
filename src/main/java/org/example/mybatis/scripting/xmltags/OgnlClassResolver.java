package org.example.mybatis.scripting.xmltags;

import ognl.ClassResolver;
import org.example.mybatis.io.Resources;

import java.util.HashMap;
import java.util.Map;

public class OgnlClassResolver implements ClassResolver {

    private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>(101);

    @Override
    public Class classForName(String className, Map context) throws ClassNotFoundException {
        Class<?> result = null;
        if ((result = classes.get(className)) == null) {
            try {
                result = Resources.classForName(className);
            } catch (ClassNotFoundException e1) {
                if (className.indexOf('.') == -1) {
                    result = Resources.classForName("java.lang." + className);
                    classes.put("java.lang." + className, result);
                }
            }
            classes.put(className, result);
        }
        return result;
    }

}
