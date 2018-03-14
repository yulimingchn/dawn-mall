package com.dawn.web.threadlocal;

import com.dawn.web.bean.User;

/**
 * @author  DAWN
 * 用户本地线程类
 */
public class UserThreadLocal {

    private static final ThreadLocal<User> LOCAL = new ThreadLocal<User>();

    private UserThreadLocal() {

    }

    public static void set(User user) {
        LOCAL.set(user);
    }

    public static User get() {
        return LOCAL.get();
    }

}
