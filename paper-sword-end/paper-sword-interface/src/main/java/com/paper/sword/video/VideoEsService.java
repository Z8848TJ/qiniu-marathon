package com.paper.sword.video;

import com.paper.sword.common.vo.EsVideo;
import com.paper.sword.common.entity.Video;

import java.util.List;

/**
 * @author wwh
 * @date 2023/10/30
 */
public interface VideoEsService {

    void saveEsVideo(EsVideo esVideo);

    List<String> getEsVideoByType(String type);

    List<String> getEsVideo(String keyWord);



}
