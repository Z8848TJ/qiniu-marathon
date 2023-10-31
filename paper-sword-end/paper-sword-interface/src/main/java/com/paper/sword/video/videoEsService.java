package com.paper.sword.video;

import com.paper.sword.common.vo.EsVideo;
import com.paper.sword.video.entity.Video;

import java.util.List;

/**
 * @author wwh
 * @date 2023/10/30
 */
public interface videoEsService {

    void saveEsVideo(EsVideo esVideo);

    List<Video> getEsVideoByType(String type);

    List<Video> getEsVideo(String keyWord);



}
