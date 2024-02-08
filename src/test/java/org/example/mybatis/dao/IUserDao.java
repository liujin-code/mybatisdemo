package org.example.mybatis.dao;

import org.example.mybatis.PO.User;

public interface IUserDao {
    User queryUserInfoById(String uId);
}
