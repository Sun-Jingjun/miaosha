package com.jingjun.dao;

import com.jingjun.domain.MiaoshaUser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 *
 */
public class TestMiaoShaUserDao {
    private static Logger logger = LoggerFactory.getLogger(TestMiaoShaUserDao.class);
    @Resource
    private MiaoShaUserDao miaoShaUserDao;
    @Test
    public void testGetUserById() {
        MiaoshaUser userById = miaoShaUserDao.getUserById(11111111111L);

    }
}
