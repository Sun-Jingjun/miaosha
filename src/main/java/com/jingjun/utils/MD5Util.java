package com.jingjun.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.jupiter.api.Test;

/**
 * MD5加密
 */
public class MD5Util {

    /**
     * 用户输入到表单时用到的salt
     */
    private static final String salt = "1a2b3c4d";

    /**
     * 进行一次MD5
     * @param data 数据
     * @return 加密后的数据
     */
    public static String md5(String data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * 输入到表单进行一次MD5
     * @param password 用户提交的密码
     * @return 一次加密后的密码
     */
    public static String inputToFormPass(String password) {
        return md5(""+salt.charAt(1) + salt.charAt(2) + password + salt.charAt(4) + salt.charAt(3));
    }

    /**
     * 表单密码到数据库密码进行一次MD5
     * @param password 表单密码
     * @param salt 辅助字符串
     * @return 加密后的数据库存储密码
     */
    public static String formPassToDBPass(String password,String salt) {
        return md5(""+salt.charAt(1) + salt.charAt(2) + password + salt.charAt(4) + salt.charAt(3));
    }


    /**
     * 输入到数据库，进行两次MD5
     * @param password 用户输入密码
     * @param salt 辅助salt
     * @return 两次加密后的密文
     */
    public static String inputToDBPass(String password,String salt) {
        String formPass = inputToFormPass(password);
        return formPassToDBPass(formPass, salt);
    }

    @Test
    public void testMD5() {
        //8e94fa448f2f1a69ae603077b813fc64
        System.out.println(inputToDBPass("11111111111","111111"));
    }
}
