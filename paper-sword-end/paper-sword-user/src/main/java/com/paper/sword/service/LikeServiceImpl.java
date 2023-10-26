package com.paper.sword.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.paper.sword.annotation.ControlsLog;
import com.paper.sword.entity.Like;
import com.paper.sword.mapper.LikeMapper;
import com.paper.sword.user.LikeService;
import com.paper.sword.user.entity.Video;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Override
    @ControlsLog(operateType = 0)
    public void LikeVideo(String userId, String videoId) {
        Like like = new Like();
        like.setVideoId(videoId);
        like.setUserId(userId);
        like.setType(0);
        likeMapper.insert(like);
    }

    @Override
    @ControlsLog(operateType = 1)
    public void CollectVideo(String userId, String videoId) {
        Like like = new Like();
        like.setVideoId(videoId);
        like.setUserId(userId);
        like.setType(1);
        likeMapper.insert(like);
    }

    @Override
    public List<Video> getLikeVideo(String userId) {
        return likeMapper.selectList(new QueryWrapper<>());
    }
}
