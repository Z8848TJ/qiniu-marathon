package com.paper.sword.vo;

/**
 * 使用 ThreadLocal 保存用户信息
 */
public class UserHolder {

    private static final ThreadLocal<UserVO> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void saveUser(UserVO user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static UserVO getUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static void removeUser() {
        USER_THREAD_LOCAL.remove();
    }

}
