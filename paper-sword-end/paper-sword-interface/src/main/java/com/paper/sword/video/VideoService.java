package com.paper.sword.video;

import com.baomidou.mybatisplus.extension.service.IService;
import com.paper.sword.common.entity.Video;

import java.util.List;

public interface VideoService extends IService<Video> {


    List<Video> videoListByUserId(Integer id);
    
}
