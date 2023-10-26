package com.paper.sword.user;

import com.paper.sword.user.entity.LikeViodeVo;
import java.util.List;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Service
public interface LikeService {

    void likeVideo(String userId, String videoId);

    void collectVideo(String userId, String videoId);

    List<LikeViodeVo> getLikeVideo(String userId);

    List<LikeViodeVo> getCollectVideo(String userId);
}
