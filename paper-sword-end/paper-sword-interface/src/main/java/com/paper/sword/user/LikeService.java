package com.paper.sword.user;

import com.paper.sword.common.vo.LikeVideoVo;
import java.util.List;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Service
public interface LikeService {

    void likeVideo(String videoId, Integer userId);

    void collectVideo(String videoId, Integer userId);

    List<LikeVideoVo> getLikeVideo(Integer userId);

    List<LikeVideoVo> getCollectVideo(Integer userId);

    void notLike(String videoId);

    void report(String videoId);

    void transfer(String videoId);
}
