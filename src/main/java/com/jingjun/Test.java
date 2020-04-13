package com.jingjun;

import com.jingjun.dao.MiaoShaUserDao;
import com.jingjun.domain.MiaoshaUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
public class Test {
    private static Logger logger = LoggerFactory.getLogger(Test.class);
    @Resource
    public static MiaoShaUserDao miaoShaUserDao;
    public static void main(String[] args) {
        MiaoshaUser userById = miaoShaUserDao.getUserById(11111111111L);
    }
}
