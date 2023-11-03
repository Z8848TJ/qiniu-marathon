package com.paper.sword.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paper.sword.common.mapper.VideoMapper;
import com.paper.sword.common.entity.Video;
import com.paper.sword.video.VideoService;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;


@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
        implements VideoService{


    @Override
    public List<Video> videoListByUserId(Integer id) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getUserId, id);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Integer getVideoCountByUrl(String url) {
        return baseMapper.selectCount(new QueryWrapper<Video>().eq("video_url", url));
    }
}




