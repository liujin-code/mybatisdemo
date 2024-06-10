package org.example.mybatis.dao;


import org.example.mybatis.PO.Activity;

public interface IActivityDao {

    Activity queryActivityById(Activity activity);
//    Integer insert(Activity activity);
}
