package org.example.mybatis;

import com.alibaba.fastjson.JSON;
import org.example.mybatis.PO.User;
import org.example.mybatis.dao.IUserDao;
import org.example.mybatis.io.Resources;
import org.example.mybatis.session.SqlSession;
import org.example.mybatis.session.SqlSessionFactory;
import org.example.mybatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.Reader;


public class MyTest {
    // 测试MapperProxyFactory的newInstance方法
    @Test
    public void test_SqlSessionFactory() {
        // 1. 从SqlSessionFactory中获取SqlSession
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        User res = userDao.queryUserInfoById("1");
        System.out.println(JSON.toJSONString(res));
    }


}
