package com.dawn.sso.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dawn.common.service.RedisService;
import com.dawn.sso.mapper.UserMapper;
import com.dawn.sso.pojo.User;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Boolean check(String param, Integer type) {
        // 校验type是否合法
        User record = new User();
        switch (type) {
        case 1:
            record.setUsername(param);
            break;
        case 2:
            record.setPhone(param);
            break;
        case 3:
            record.setEmail(param);
            break;
        default:
            return null;
        }
        return this.userMapper.selectOne(record) == null;
    }

    public Boolean doRegister(User user) {
        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());

        // 密码进行加密处理
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));

        return this.userMapper.insert(user) == 1;
    }

    public String doLogin(String username, String password) throws Exception {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (null == user) {
            return null;
        }
        // 判断密码是否正确
        if (!StringUtils.equals(user.getPassword(), DigestUtils.md5Hex(password))) {
            return null;
        }

        // 登录成功了
        String token = DigestUtils.md5Hex(System.currentTimeMillis() + username);
        // 将当前的用户数据写入到redis中
        this.redisService.set("TOKEN_" + token, MAPPER.writeValueAsString(user), 1800);
        return token;
    }

    public User queryUserByToken(String token) {
        String key = "TOKEN_" + token;
        String jsonData = this.redisService.get(key);
        if (StringUtils.isEmpty(jsonData)) {
            // 登录超时
            return null;
        }
        try {
            // 非常重要！！！
            this.redisService.expire(key, 1800);
            return MAPPER.readValue(jsonData, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
