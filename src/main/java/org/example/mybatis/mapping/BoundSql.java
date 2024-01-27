package org.example.mybatis.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoundSql {
    private String parameterType;
    private String resultType;
    private String sql;
    private Map<Integer, String> parameterMap;
}
