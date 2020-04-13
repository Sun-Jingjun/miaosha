package com.jingjun.service;

import com.jingjun.dao.UserDao;
import com.jingjun.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class UserService {

    @Resource
    UserDao userDao;

    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }

    @Transactional
    public Boolean tx() {
        User user1 = new User();
        user1.setId(2);
        user1.setName("22");
        Integer result1 = userDao.insertUser(user1);

        User user2 = new User();
        user2.setId(1);
        user2.setName("111");
        Integer result2 = userDao.insertUser(user2);

        return true;
    }
}
