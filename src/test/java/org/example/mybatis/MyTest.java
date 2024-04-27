package org.example.mybatis;

import com.alibaba.fastjson.JSON;
import org.example.mybatis.PO.User;
import org.example.mybatis.dao.IUserDao;
import org.example.mybatis.datasource.pooled.PooledDataSource;
import org.example.mybatis.io.Resources;
import org.example.mybatis.session.SqlSession;
import org.example.mybatis.session.SqlSessionFactory;
import org.example.mybatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;


public class MyTest {
    private final Logger logger = LoggerFactory.getLogger(MyTest.class);

    // 测试MapperProxyFactory的newInstance方法
    @Test
    public void test_SqlSessionFactory() {
        // 1. 从SqlSessionFactory中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        for (int i = 0; i < 50; i++) {
            User user = userDao.queryUserInfoById("1");
            logger.info("测试结果：{}", JSON.toJSONString(user));
        }
    }

    @Test
    public void test_pooled() throws SQLException, InterruptedException {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriver("com.mysql.jdbc.Driver");
        pooledDataSource.setUrl("jdbc:mysql://localhost:3306/mybatis?useUnicode=true");
        pooledDataSource.setUsername("root");
        pooledDataSource.setPassword("root");
        // 持续获得链接
        while (true) {
            Connection connection = pooledDataSource.getConnection();
            System.out.println(connection);
            Thread.sleep(1000);
            connection.close();
        }
    }
}
