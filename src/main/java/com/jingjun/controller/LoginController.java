package com.jingjun.controller;

import com.jingjun.domain.MiaoshaUser;
import com.jingjun.result.CodeMsg;
import com.jingjun.result.Result;
import com.jingjun.service.MiaoShaUserService;
import com.jingjun.utils.MD5Util;
import com.jingjun.utils.ValidatorUtil;
import com.jingjun.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private MiaoShaUserService miaoShaUserService;

    @RequestMapping("/tologin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/dologin")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        logger.info(loginVo.toString());
        //登录
        String token = miaoShaUserService.login(response,loginVo);
        return Result.success(token);
    }
}
