package com.paper.sword.task;

import com.paper.sword.common.vo.MarkVo;
import com.paper.sword.video.RecommendedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wwh
 * @date 2023/11/1
 */
@Component
public class UserSimilarity {
    @Autowired
    private RecommendedService recommendedService;

    @Scheduled(cron = "0 0 0 * * ? ")
    public void mark(){
        Map<Integer, MarkVo> recommend = recommendedService.getRecommend();
        recommendedService.getSimilar(recommend);
    }
}
