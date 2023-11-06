package com.paper.sword.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.paper.sword.common.annotation.ControlsLog;
import com.paper.sword.common.vo.LikeVideoVo;
import java.util.List;

import com.paper.sword.user.entity.Like;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Service
public interface LikeService extends IService<Like> {

    /**
     * 点赞
     */
    @ControlsLog()
    void likeVideo(String videoId, Integer fromId,  Integer toId, Integer type);

    /**
     * 收藏
     */
    void collectVideo(String videoId, Integer fromId,  Integer toId, Integer type);

    /**
     * 用户是否点赞，是否收藏
     */
    List<Boolean> isLikeAndIsCollect(String videoId, Integer userId);

    /**
     * 用户喜欢视频列表
     */
    List<LikeVideoVo> getLikeVideo(Integer userId);

    /**
     * 用户收藏视频列表
     */
    List<LikeVideoVo> getCollectVideo(Integer userId);

    /**
     * 不喜欢
     */
    void notLike(String videoId);

    /**
     * 举报
     */
    void report(String videoId);

    /**
     * 转发
     */
    void transfer(String videoId);

    /**
     * 视频点赞，收藏， 评论数
     */
    List<Integer> videoInfoCount(String videoId);

    /**
     * 用户获赞
     */
    Integer gainLikeCount(Integer id);
}
