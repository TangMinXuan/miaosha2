package com.tmx.miaosha2.service;

import com.tmx.miaosha2.DAO.DO.UserDO;
import com.tmx.miaosha2.DAO.UserDAO;
import com.tmx.miaosha2.result.ErrorMessage;
import com.tmx.miaosha2.result.GlobalException;
import com.tmx.miaosha2.redis.RedisService;
import com.tmx.miaosha2.redis.key.TokenKey;
import com.tmx.miaosha2.util.MD5Util;
import com.tmx.miaosha2.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class LoginService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    RedisService redisService;

    public String login(String email, String password, HttpServletResponse response) {
        //判断用户存不存在
        UserDO userDO = userDAO.getUserByEmail(email);
        if(userDO == null) {
            throw new GlobalException(ErrorMessage.ACCOUNT_NOT_EXIST);
        }

        //检查密码是否正确
        int salt = userDO.getUserSalt();
        String inputAddSalt = MD5Util.addSalt(password, salt);
        if(!inputAddSalt.equals(userDO.getUserPassword())) {
            throw new GlobalException(ErrorMessage.PASSWORD_ERROR);
        }

        //生产Cookie
        String token = UUIDUtil.getUUID();
        addCookie(response, token, userDO);
        return token;
    }

    public UserDO getByToken(HttpServletResponse response, String token) {
        TokenKey key = new TokenKey(token);
        UserDO user = redisService.get(key.getKey(), UserDO.class);
        if(user == null) {
            return null;
        }
        //刷新cookie和redis的过期时间
        addCookie(response, token, user);
        return user;
    }

    private void addCookie(HttpServletResponse response, String token, UserDO user) {
        TokenKey key = new TokenKey(token);
        redisService.set(key.getKey(), user, key.getSeconds());
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(key.getSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
