package org.example.mybatis.type;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public enum JdbcType {
    INTEGER(Types.INTEGER),
    FLOAT(Types.FLOAT),
    DOUBLE(Types.DOUBLE),
    DECIMAL(Types.DECIMAL),
    VARCHAR(Types.VARCHAR),
    TIMESTAMP(Types.TIMESTAMP);

    private static Map<Integer,JdbcType> codeLookup = new HashMap<>();
    private final int TYPE_CODE;

    static {
        for (JdbcType type : JdbcType.values()) {
            codeLookup.put(type.TYPE_CODE, type);
        }
    }
    JdbcType(int code) {
        this.TYPE_CODE = code;
    }

    public JdbcType forCode(int code) {
        return codeLookup.get(code);
    }
}
