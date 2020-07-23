package com.tmx.miaosha2.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;

public class MD5Util {

    private static final String FIXED_SALT = "1a2b3c4d";

    private MD5Util() {}

    private static String toMD5(String str) {
        return DigestUtils.md5Hex(str);
    }

    /**
     * 给传入的str固定加盐
     */
    public static String fixedAddSalt(String str) {
        String offsetStr = "" + FIXED_SALT.charAt(0) + FIXED_SALT.charAt(2) + str +
                FIXED_SALT.charAt(5) + FIXED_SALT.charAt(4);
        return toMD5(offsetStr);
    }

    /**
     * 给传入的str随机加盐
     */
    public static String randomlyAddSalt(String str) {
        Random r = new Random();
        int salt = r.nextInt(100) + 10;
        System.out.println("slat: " + salt);
        String offsetStr = str + salt;
        return toMD5(offsetStr);
    }

    /**
     * 给传入的str加传入的盐
     */
    public static String addSalt(String str, int salt) {
        String offsetStr = str + salt;
        return toMD5(offsetStr);
    }

    public static void main(String[] args) {
        String password = "123456";
        String first = fixedAddSalt(password);
        String second = addSalt(first, 15);
        System.out.println("first = " + first);
        System.out.println("second = " + second);
    }
}
