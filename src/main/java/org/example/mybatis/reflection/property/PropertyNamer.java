package org.example.mybatis.reflection.property;

import java.util.Locale;

/**
 * 属性命名器
 */
public class PropertyNamer {

    public PropertyNamer() {

    }

    /**
     * 方法转换为属性
     */
    public static String methodToProperty(String name) {
        if(name.startsWith("is")){
            name = name.substring(2);
        } else if (name.startsWith("get")||name.startsWith("set")) {
            name = name.substring(3);
        } else {
            throw new RuntimeException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
        }

        /*
         * 如果只有1个字母，转换为小写
         * 如果大于1个字母，第二个字母非大写，转换为小写
         */
        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }

    /**
     * 是否为属性
     * 判断开头是否为set/get/is
     */
    public static boolean isProperty(String name) {
        return isGetter(name) || isSetter(name);
    }

    /**
     * 是否为setter
     * @param name
     * @return
     */
    private static boolean isSetter(String name) {
        return name.startsWith("set") && name.length() > 3;
    }

    /**
     * 是否为getter
     */
    private static boolean isGetter(String name) {
        return name.startsWith("get") && name.length() > 3;
    }
}
