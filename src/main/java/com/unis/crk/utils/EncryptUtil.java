package com.unis.crk.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密 使用Security密码加密
 * @author pengxl
 * @version 1.0
 * 创建时间: 2019/05/09 14:27
 */
public class EncryptUtil {


    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 密码加密
     * @param charSequence 明文密码
     * @return 返回加密后密码
     */
    public static String encrypt(CharSequence charSequence) {
        return encoder.encode(charSequence);
    }

    /**
     * 密码比较
     * @param charSequence 明文密码
     * @param str 加密密码
     * @return 返回验证结果
     */
    public static boolean matches(CharSequence charSequence,String str) {
        return encoder.matches(charSequence,str);
    }

    public static void main(String[] args) {
        String pwd = EncryptUtil.encrypt("admin");
        System.out.println(pwd);
        System.out.println(EncryptUtil.matches("admin",pwd));

    }
}
