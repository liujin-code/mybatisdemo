package org.example.mybatis.scripting.xmltags;

import java.util.List;

public class MixedSqlNode implements SqlNode {
    //组合模式，拥有一个SqlNode的List
    private final List<SqlNode> contents;

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override
    public boolean apply(DynamicContext context) {
        // 依次调用list里每个元素的apply
        contents.forEach(node -> node.apply(context));
        return true;
    }

}
