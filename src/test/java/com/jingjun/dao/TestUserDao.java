package com.jingjun.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public class TestUserDao {
    @Autowired
    UserDao userDao;

    @Test
    public void testGetUserById(){
        userDao.getUserById(1);
    }
}
