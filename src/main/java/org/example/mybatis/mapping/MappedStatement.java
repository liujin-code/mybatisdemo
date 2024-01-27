package org.example.mybatis.mapping;

import lombok.Data;
import org.example.mybatis.session.Configuration;


@Data
public class MappedStatement {
    private Configuration configuration;
    private String id;
    private SqlCommandType sqlCommandType;
    private BoundSql boundSql;

    MappedStatement() {
        // constructor disabled
    }

    public static class Builder{
        private final MappedStatement mappedStatement = new MappedStatement();
        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType,BoundSql boundSql) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.boundSql = boundSql;
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }
    }
}
