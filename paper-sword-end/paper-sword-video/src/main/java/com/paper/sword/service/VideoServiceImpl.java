package com.paper.sword.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paper.sword.video.entity.Video;
import com.paper.sword.video.VideoService;
import com.paper.sword.mapper.VideoMapper;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;


@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{

    @Override
    public List<Video> videoListByUserId(String id) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getUserId, id);
        return baseMapper.selectList(wrapper);
    }
}




