package com.paper.sword.task;

import com.paper.sword.common.entity.Video;
import com.paper.sword.common.util.RedisUtil;
import com.paper.sword.common.vo.MarkVo;
import com.paper.sword.user.LikeService;
import com.paper.sword.video.RecommendedService;
import com.paper.sword.video.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wwh
 * @date 2023/11/1
 */
@Component
@Slf4j
public class PaperSwordTask {
    
    @Reference
    private RecommendedService recommendedService;
    
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    
    @Reference
    private VideoService videoService;
    
    @Reference
    private LikeService likeService;
    
    // 时间纪元
    private static final Date epoch;
    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse("2023-11-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("时间纪元初始化失败", e);
        }
    }
    

    // 每一小时执行一次 
    @Scheduled(cron = "0 0 0 * * ? ")
    public void mark(){
        Map<Integer, MarkVo> recommend = recommendedService.getRecommend();
        recommendedService.getSimilar(recommend);
    }

    @Scheduled(cron = "0/30 * * * * ?")
    public void calcVideoScore() {
        String scoreKey = RedisUtil.getVideoScoreKey();
        BoundSetOperations<String, Object> operations = 
                redisTemplate.boundSetOps(scoreKey);
        
        if(operations.size() == 0) {
            log.info("任务取消，没有需要刷新的视频");
            return;
        }
        
        log.info("任务开始，正在计算视频分数");
        
        while(operations.size() > 0) {
            refresh((String)operations.pop());
        }


        log.info("任务结束，视频分数计算完成");
    }

    
    private void refresh(String videId) {
        Video video = videoService.getById(videId);

        List<Integer> count = likeService.videoInfoCount(videId);
        
        // 计算权重
        double w = count.get(0) * 2 + count.get(1) * 10 + count.get(2) * 5;
        
        // 分数 = 帖子权重 + 距离天数
        double score = Math.log10(Math.max(w, 1)) +
                (double) (video.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);
        
        // 更新分数
        video.setScore(score);
        videoService.updateById(video);
    }
}
