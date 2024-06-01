package org.example.mybatis.builder;

import org.example.mybatis.mapping.ResultMap;
import org.example.mybatis.mapping.ResultMapping;

import java.util.List;

/**
 * 结果映射解析器
 */
public class ResultMapResolver {
    private final MapperBuilderAssistant assistant;

    private final String id;

    private final Class<?> type;

    private final List<ResultMapping> resultMappings;

    public ResultMapResolver(MapperBuilderAssistant assistant, String id, Class<?> type, List<ResultMapping> resultMappings) {
        this.assistant = assistant;
        this.id = id;
        this.type = type;
        this.resultMappings = resultMappings;
    }

    public ResultMap resolve() {
        return assistant.addResultMap(this.id, this.type, this.resultMappings);
    }
}
