package com.paper.sword.video;

import com.paper.sword.common.vo.MarkBo;
import com.paper.sword.common.vo.MarkVo;
import com.paper.sword.common.vo.Similarity;

import java.util.*;
import java.util.Map;

/**
 * @author wwh
 * @date 2023/11/1
 */
public interface RecommendedService {

    public Map<Integer, MarkVo> getRecommend();

    public List<Integer> getVideoTypeByUserId(Integer userId);

    public void getSimilar(Map<Integer,MarkVo> data);

    public List<Similarity> getSimilarityByUser(Integer userId);
}
