package com.paper.sword.server;

import com.paper.sword.entity.Like;
import com.paper.sword.mapper.LikeMapper;
import com.paper.sword.user.LikeService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Override
    public void LikeVideo(String userId, String videoId) {
        Like like = new Like();
        like.setVideoId(videoId);
        like.setUserId(userId);
        like.setType(0);
        likeMapper.insert(like);
    }

    @Override
    public void CollectVideo(String userId, String videoId) {
        Like like = new Like();
        like.setVideoId(videoId);
        like.setUserId(userId);
        like.setType(1);
        likeMapper.insert(like);
    }
}
