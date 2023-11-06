package com.paper.sword.video;

import com.baomidou.mybatisplus.extension.service.IService;
import com.paper.sword.common.entity.Video;

import java.util.List;

public interface VideoService extends IService<Video> {


    /**
     * 作品列表
     */
    List<Video> videoListByUserId(Integer id);

    Video getVideoCountByUrl(String url);

    void completeVideo(List<Video> esVideoList);

    List<Video> getHotVideo(Integer begin);
}
