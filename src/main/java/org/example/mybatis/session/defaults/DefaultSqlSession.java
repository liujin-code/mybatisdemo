package org.example.mybatis.session.defaults;

import com.alibaba.fastjson.JSON;
import org.example.mybatis.Executor.Executor;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.RowBounds;
import org.example.mybatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private final Logger logger = LoggerFactory.getLogger(DefaultSqlSession.class);

    private final Configuration configuration;

    private final Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) throws SQLException {
        logger.info("执行查询 statement：{} parameter：{}", statement, JSON.toJSONString(parameter));
        MappedStatement ms = configuration.getMappedStatement(statement);
        List<T> list = executor.query(ms, parameter, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER, ms.getSqlSource().getBoundSql(parameter));
        return list.get(0);
    }

//    private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<?> clazz) {
//        List<T> list = new ArrayList<>();
//        try {
//            ResultSetMetaData metaData = resultSet.getMetaData();
//            int columnCount = metaData.getColumnCount();
//            while (resultSet.next()) {
//                T o = (T) clazz.newInstance();
//                for (int i = 1; i <= columnCount; i++) {
//                    Object object = resultSet.getObject(i);
//                    String columnName = metaData.getColumnName(i);
//                    String methodName = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
//                    Method method;
//                    if (object instanceof Timestamp) {
//                        method = clazz.getMethod(methodName, Date.class);
//                    } else {
//                        method = clazz.getMethod(methodName, object.getClass());
//                    }
//                    method.invoke(o, object);
//                }
//                list.add(o);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return list;
//    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！" + "方法：" + statement);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
