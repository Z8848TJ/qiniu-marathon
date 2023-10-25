package com.paper.sword.common.vo;

/**
 * 使用 ThreadLocal 保存用户信息
 */
public class UserHolder {

    private static final InheritableThreadLocal<UserVO> USER_THREAD_LOCAL = new InheritableThreadLocal<>();

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
