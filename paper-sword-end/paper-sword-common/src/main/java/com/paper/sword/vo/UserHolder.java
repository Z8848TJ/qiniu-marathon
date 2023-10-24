package com.paper.sword.vo;


public class UserHolder {

    private static final ThreadLocal<User> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void saveUser(User user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static User getUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static void removeUser() {
        USER_THREAD_LOCAL.remove();
    }

}
