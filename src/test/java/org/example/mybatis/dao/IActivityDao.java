package org.example.mybatis.dao;


import org.example.mybatis.PO.Activity;

public interface IActivityDao {

    Activity queryActivityById(Long activityId);

    Integer insert(Activity activity);
}
