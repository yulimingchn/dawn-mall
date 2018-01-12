package com.dawn.sso.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dawn.common.utils.CookieUtils;
import com.dawn.sso.pojo.User;
import com.dawn.sso.service.UserService;

@RequestMapping("user")
@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public static final String COOKIE_NAME = "TT_TOKEN";

    @Autowired
    private UserService userService;

    /**
     * 去注册页面
     * 
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String toRegister() {
        return "register";
    }

    /**
     * 去登录页面
     * 
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    /**
     * 检测数据是否可用
     * 
     * @param param
     * @param type
     * @return
     */
    @RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> check(@PathVariable("param") String param,
            @PathVariable("type") Integer type) {
        Boolean bool = this.userService.check(param, type);
        if (null == bool) {
            // 请求参数有误
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        // 前端工程师（张三）太屌了，不愿意修改，没有办法，只能通过后台的逻辑解决，无奈啊。。。。。。。
        return ResponseEntity.ok(!bool);
    }

    /**
     * 实现注册
     * 
     * @param user
     * @return
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doRegister(@Valid User user, BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (bindingResult.hasErrors()) {
            // 校验没有通过
            List<String> msgs = new ArrayList<String>();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError : allErrors) {
                msgs.add(objectError.getDefaultMessage());
            }
            result.put("data", StringUtils.join(msgs, '|'));
            result.put("status", "400");
            return result;
        }
        Boolean bool = this.userService.doRegister(user);
        if (bool) {
            // 注册成功
            result.put("status", "200");
        } else {
            // 注册失败
            result.put("status", "300");
            result.put("data", "  哈哈~~");
        }
        return result;
    }

    /**
     * 实现登录
     * 
     * @param user
     * @return
     */
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doLogin(User user, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String token = this.userService.doLogin(user.getUsername(), user.getPassword());
            if (StringUtils.isEmpty(token)) {
                // 登录失败
                result.put("status", 300);
            } else {
                // 登录成功
                result.put("status", 200);
                // 将token写入到cookie中
                CookieUtils.setCookie(request, response, COOKIE_NAME, token);
            }
        } catch (Exception e) {
            LOGGER.error("登录失败！ User = " + user, e);
            // 处理业务逻辑
            result.put("status", 500);
        }
        return result;
    }

    /**
     * 根据token查询当前用户的登录信息
     * 
     * @param token
     * @return
     */
    @RequestMapping(value = "{token}", method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByToken(@PathVariable("token") String token) {
        try {
            User user = this.userService.queryUserByToken(token);
            if (null == user) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
