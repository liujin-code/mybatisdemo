package org.example.mybatis.scripting.xmltags;

import org.dom4j.Element;
import org.example.mybatis.builder.BaseBuilder;
import org.example.mybatis.mapping.SqlSource;
import org.example.mybatis.scripting.defaults.RawSqlSource;
import org.example.mybatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * XML脚本构建器
 */
public class XMLScriptBuilder extends BaseBuilder {
    private final Element element;

    private boolean isDynamic;

    private final Class<?> parameterType;

    public XMLScriptBuilder(Configuration configuration, Element element, Class<?> parameterType) {
        super(configuration);
        this.element = element;
        this.parameterType = parameterType;
    }

    public SqlSource parseScriptNode() {
        List<SqlNode> contents = parseDynamicTags(element);
        MixedSqlNode rootSqlNode = new MixedSqlNode(contents);
        return new RawSqlSource(configuration, rootSqlNode, parameterType);
    }

    List<SqlNode> parseDynamicTags(Element element){
        List<SqlNode> contents = new ArrayList<>();
        // element.getText 拿到 SQL
        String data = element.getText();
        contents.add(new StaticTextSqlNode(data));
        return contents;
    }
}
