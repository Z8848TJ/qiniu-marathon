package com.paper.sword.video;

import java.util.Map;

/**
 * @author wwh
 * @date 2023/11/1
 */
public interface RecommendedService {

    public Map<Integer,Integer> getRecommend(Integer userId);
}
