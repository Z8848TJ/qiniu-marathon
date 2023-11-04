package com.paper.sword.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paper.sword.common.mapper.VideoMapper;
import com.paper.sword.common.entity.Video;
import com.paper.sword.video.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


@Service
@Slf4j
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
        implements VideoService{

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    @Override
    public List<Video> videoListByUserId(Integer id) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getUserId, id);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Video getVideoCountByUrl(String url) {
        return baseMapper.selectOne(new QueryWrapper<Video>().eq("video_url", url));
    }

    @Override
    public void completeVideo(List<Video> esVideoList) {
        // 获取视频的总数
        Integer count = query().count();

        int begin = RANDOM.nextInt(Math.max(count - 10, 0));
        int end = begin + 10 < count ? begin + 10 : count;

        // 随机查询视频
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.last("limit " + begin + " ," + end);
        List<Video> videos = baseMapper.selectList(wrapper);


        // 视频去重
        HashSet<String> set = new HashSet<>();
        for (Video video : esVideoList) {
            set.add(video.getId());
        }

        for (Video video : videos) {
            if(esVideoList.size() == 10) {
                break;
            }
            
            if(!set.contains(video.id)) {
                esVideoList.add(video);
            }
        }
    }
}




