package com.paper.sword.controller.video;

import com.paper.sword.common.entity.Dict;
import com.paper.sword.common.mapper.DictMapper;
import com.paper.sword.common.util.RedisUtil;
import com.paper.sword.common.vo.*;
import com.paper.sword.config.QiniuConfig;
import com.paper.sword.user.CommentService;
import com.paper.sword.user.LikeService;
import com.paper.sword.user.entity.Comment;
import com.paper.sword.video.RecommendedService;
import com.paper.sword.video.VideoEsService;
import com.paper.sword.video.VideoService;
import com.paper.sword.common.entity.Video;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/video")
@Slf4j
public class VideoController {
    
    @Reference
    private VideoService videoService;
    
    @Reference
    private LikeService likeService;

    @Reference
    private RecommendedService recommendedService;

    @Reference
    private VideoEsService esService;
    
    @Reference
    private CommentService commentService;
    
    @Autowired
    private DictMapper dictMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private QiniuConfig qiniuConfig;

    /**
     * 获取视频点赞数，收藏数，评论数
     */
    @GetMapping("/count")
    public Result listCount(@RequestParam String videoId, @RequestParam Integer userId) {
        UserVO user = UserHolder.getUser();
        List<Integer> counts = likeService.videoInfoCount(videoId);
        
        List<Boolean> res = new ArrayList<>();
        if (userId == 0) {
            res.add(false);
            res.add(false);
        } else {
            log.info("查询点赞收藏");
            res = likeService.isLikeAndIsCollect(videoId, userId);
        }
        
        return Result.success()
                .data("counts", counts)
                .data("likeAndCollect", res);
    }


    /**
     * 视频推荐
     * @return 推荐视频列表
     */

    @GetMapping("/recommend")
    public Result recommend() {
        UserVO user = UserHolder.getUser();
        List<Similarity> similarityByUser = new ArrayList<>();
        if(user != null) {
            similarityByUser =  recommendedService.getSimilarityByUser(user.getId());
        } 
        
        HashSet<Integer> strings = new HashSet<>();
        for (Similarity similarity : similarityByUser) {
            List<Integer> videoTypeByUserId = recommendedService.getVideoTypeByUserId(similarity.userTwo);
            for (Integer i : videoTypeByUserId) {
                strings.add(i);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (Integer string : strings) {
            if(i!=strings.size() - 1){
                stringBuilder.append(string+",");
                i++;
            }else{
                stringBuilder.append(string);
            }
        }
        List<Video> videoList = esService.getEsVideoByType(stringBuilder.toString());
        
        return Result.success().data(videoList);
    }

    /**
     * 获取视频信息
     */
    @PostMapping("/videoInfo")
    public Result videoInfo(@RequestBody Video video) {
        // 更新视频信息
        video.setUsername(UserHolder.getUser().getUsername());
        video.setCreateTime(new Date());
        videoService.updateById(video);
        
        // 将视频信息存入 es
        Video byId = videoService.getById(video.getId());
        EsVideo esVideo = new EsVideo();
        esVideo.setId(UUID.randomUUID().toString());
        esVideo.setVideoId(byId.getId());
        esVideo.setVideoType(byId.getVideoType());
        esVideo.setUsername(UserHolder.getUser().getUsername());
        esVideo.setDescription(byId.getDescription());
        esVideo.setCreateTime(byId.getCreateTime());
        esService.saveEsVideo(esVideo);
        
        // 计算视频分数
        String scoreKey = RedisUtil.getVideoScoreKey();
        redisTemplate.opsForSet().add(scoreKey, video.getId());

        return Result.success().data("投稿成功");
    }

    /**
     * 获取视频评论
     */
    @GetMapping("/comment")
    public Result videoComment(@RequestParam String videoId, @RequestParam Integer begin) {
        List<CommentVo> list = commentService.getParentComment(videoId, begin);

        for (CommentVo commentVo : list) {
            commentVo.setHeaderUrl(qiniuConfig.getHeaderBucketUrl() + commentVo.getHeaderUrl());
        }
        
        return Result.success().data(list);
    }

    /**
     * 获取二级评论
     */
    @GetMapping("/videoChildrenComment")
    public Result childrenComment(@RequestParam String commentId) {
        List<Comment> list = commentService.getChildrenComment(commentId);
        
        return Result.success().data(list);
    }

    /**
     * 搜索视频
     */
    @RequestMapping("/search")
    public Result recommend(String keyword){

        List<String> videoIdList = esService.getEsVideo(keyword);
        ArrayList<Video> videoList = new ArrayList<>();
        for (String videoId : videoIdList) {
            Video byId = videoService.getById(videoId);
            videoList.add(byId);
        }
        return Result.success().data(videoList);
    }


    /**
     * 获取视频分类标签
     */
    @GetMapping("/videoLab")
    public Result videoLab() {
        List<Dict> videoTypeList = dictMapper.getVideoType();
        
        return Result.success().data(videoTypeList);
    }

    /**
     * 格局视频标签获取视频，视频分类
     */
    @GetMapping("/type")
    public Result videoByType(@RequestParam String type) {
        List<Video> videoTypeList = esService.getEsVideoByType(type);

        return Result.success().data(videoTypeList);
    }

    /**
     * 热门视频
     */
    @GetMapping("/hot")
    public Result HotVideo(@RequestParam(defaultValue = "0") Integer begin) {
        List<Video> list = videoService.getHotVideo(begin);
        
        return Result.success().data(list);
    }
    

}
