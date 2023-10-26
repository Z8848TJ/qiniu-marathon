package com.paper.sword.user;

import com.paper.sword.user.entity.Video;
import java.util.List;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Service
public interface LikeService {

    public void LikeVideo(String userId, String videoId);

    public void CollectVideo(String userId, String videoId);

    public List<Video> getLikeVideo(String userId);
}
