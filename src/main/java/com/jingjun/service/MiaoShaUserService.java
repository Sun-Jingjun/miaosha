package com.jingjun.service;

import com.jingjun.dao.MiaoShaUserDao;
import com.jingjun.domain.MiaoshaUser;
import com.jingjun.domain.User;
import com.jingjun.exception.GlobalException;
import com.jingjun.redis.MiaoShaUserKey;
import com.jingjun.redis.RedisService;
import com.jingjun.result.CodeMsg;
import com.jingjun.utils.MD5Util;
import com.jingjun.utils.UUIDUtil;
import com.jingjun.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@Service
public class MiaoShaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Resource
    private MiaoShaUserDao miaoShaUserDao;

    @Resource
    private RedisService redisService;

    public MiaoshaUser getUserById(Long userId) {
        //取缓存
        MiaoshaUser miaoshaUser = redisService.get(MiaoShaUserKey.getById, "" + userId, MiaoshaUser.class);
        if(miaoshaUser != null) {
            return miaoshaUser;
        }
        miaoshaUser = miaoShaUserDao.getUserById(userId);
        //更新缓存
        if(miaoshaUser != null) {
            redisService.set(MiaoShaUserKey.getById,""+userId,miaoshaUser);
        }
        return miaoshaUser;
    }

    public boolean updatePassword(String token,Long userId,String password) {
        MiaoshaUser userById = getUserById(userId);
        if(userById == null) {
            throw new GlobalException(CodeMsg.MOBILE_NULL);
        }
        //调用dao，更新数据库
        MiaoshaUser miaoshaUser = new MiaoshaUser();
        miaoshaUser.setPassword(MD5Util.formPassToDBPass(password,userById.getSalt()));
        miaoShaUserDao.update(miaoshaUser);
        //更新分布式session，对象缓存
        userById.setPassword(password);
        redisService.set(MiaoShaUserKey.token,token,userById);
        redisService.delete(MiaoShaUserKey.getById,""+userId);
        return true;
    }

    public String login(HttpServletResponse response,LoginVo loginVo) {
        String inputMobile = loginVo.getMobile();
        String inputPassword = loginVo.getPassword();
        MiaoshaUser user = getUserById(Long.valueOf(inputMobile));
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPassword = user.getPassword();
        String salt = user.getSalt();
        inputPassword = MD5Util.formPassToDBPass(inputPassword,salt);
        if(!dbPassword.equals(inputPassword)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        //在缓存中设置分布式session
        redisService.set(MiaoShaUserKey.token,token,user);
        //添加cookie到响应中
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public MiaoshaUser getUserByToken(HttpServletResponse response,String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(MiaoShaUserKey.token, token, MiaoshaUser.class);
//        System.out.println("token"+":"+token);
        //延长登录时间，刷新cookie，刷新缓存
        if(miaoshaUser != null) {
            addCookie(response,token,miaoshaUser);
        }
        return miaoshaUser;
    }
}
