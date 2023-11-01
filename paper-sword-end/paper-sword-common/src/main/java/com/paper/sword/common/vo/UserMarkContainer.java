package com.paper.sword.common.vo;

import org.apache.catalina.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wwh
 * @date 2023/11/1
 */
public class UserMarkContainer {

    private UserMarkContainer userMarkContainer = new UserMarkContainer();
    public ConcurrentHashMap<String,List<VideoMarkVo>> Container = new ConcurrentHashMap<>();

    private UserMarkContainer(){

    }

    public static UserMarkContainer getIntence(){
        return getIntence().userMarkContainer;
    }

}
