package org.example.mybatis.reflection.property;

import java.util.Iterator;

/**
 * 属性分解标记
 */
public class PropertyTokenizer implements Iterable<PropertyTokenizer>, Iterator<PropertyTokenizer> {
    // 例子：班级[0].学生.成绩
    // 班级
    private String name;

    // 班级[0]
    private String indexedName;

    // 0
    private String index;

    // 学生.成绩
    private String children;

    public PropertyTokenizer(String fullName) {
        // 例子：班级[0].学生.成绩
        // 找这个.
        int delim = fullName.indexOf('.');
        if (delim > -1) {
            this.name = fullName.substring(0, delim);
            this.children = fullName.substring(delim + 1);
        } else {
            this.name = fullName;
            this.children = null;
        }
        indexedName = name;
        // 把中括号里的数字给解析出来
        delim = name.indexOf('[');
        if (delim > -1) {
            index = name.substring(delim + 1, name.length() - 1);
            name = name.substring(0, delim);
        }
    }

    @Override
    public Iterator<PropertyTokenizer> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return children != null;
    }


    @Override
    public PropertyTokenizer next() {
        return new PropertyTokenizer(children);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported, as it has no meaning in the context of properties.");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndexedName() {
        return indexedName;
    }

    public void setIndexedName(String indexedName) {
        this.indexedName = indexedName;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }
}
