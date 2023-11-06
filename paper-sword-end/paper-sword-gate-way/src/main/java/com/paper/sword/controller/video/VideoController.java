package com.paper.sword.controller.video;

import com.paper.sword.common.vo.*;
import com.paper.sword.user.CommentService;
import com.paper.sword.user.LikeService;
import com.paper.sword.user.entity.Comment;
import com.paper.sword.video.RecommendedService;
import com.paper.sword.video.VideoEsService;
import com.paper.sword.video.VideoService;
import com.paper.sword.common.entity.Video;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获取视频点赞数，收藏数，评论数
     */
    @GetMapping("/count")
    public Result listCount(@RequestParam String videoId) {
        UserVO user = UserHolder.getUser();
        List<Integer> counts = likeService.videoInfoCount(videoId);
        
        List<Boolean> res = new ArrayList<>();
        if (user == null) {
            res.add(false);
            res.add(false);
        } else {
            res = likeService.isLikeAndIsCollect(videoId, user.getId());
        }
        
        return Result.success()
                .data("counts", counts)
                .data("likeAndCollect", res);
    }



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

    @PostMapping("/videoInfo")
    public Result videoInfo(@RequestBody Video video) {
        video.setUsername(UserHolder.getUser().getUsername());
        video.setCreateTime(new Date());
        videoService.updateById(video);
        Video byId = videoService.getById(video.getId());
        EsVideo esVideo = new EsVideo();
        esVideo.setId(UUID.randomUUID().toString());
        esVideo.setVideoId(byId.getId());
        esVideo.setVideoType(byId.getVideoType());
        esVideo.setUsername(UserHolder.getUser().getUsername());
        esVideo.setDescription(byId.getDescription());
        esVideo.setCreateTime(byId.getCreateTime());
        esService.saveEsVideo(esVideo);

        return Result.success().data("投稿成功");
    }
    
    @GetMapping("/videoComment")
    public Result videoComment(@RequestParam String videoId) {
        List<Comment> list = commentService.getParentComment(videoId);
        
        return Result.success().data(list);
    }
    
    @GetMapping("/videoChildrenComment")
    public Result childrenComment(@RequestParam String commentId) {
        List<Comment> list = commentService.getChildrenComment(commentId);
        
        return Result.success().data(list);
    }

    @RequestMapping("/search")
    public Result recommend(String keyword){

        List<String> searchVideo = esService.getEsVideo(keyword);
        ArrayList<Video> videos = new ArrayList<>();
        for (String s : searchVideo) {
            Video byId = videoService.getById(s);
            videos.add(byId);
        }
        return Result.success().data(searchVideo);
    }




}
