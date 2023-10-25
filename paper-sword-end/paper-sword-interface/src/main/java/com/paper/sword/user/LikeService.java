package com.paper.sword.user;

import org.apache.dubbo.config.annotation.Service;

/**
 * @author wwh
 * @date 2023/10/25
 */
public interface LikeService {

    public void LikeVideo(String userId, String videoId);

    public void CollectVideo(String userId, String videoId);
}
