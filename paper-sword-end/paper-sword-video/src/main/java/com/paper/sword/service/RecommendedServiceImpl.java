package com.paper.sword.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.paper.sword.common.mapper.MarkMapper;
import com.paper.sword.common.mapper.SimilarityMapper;
import com.paper.sword.common.vo.MarkVo;
import com.paper.sword.common.vo.Similarity;
import com.paper.sword.video.RecommendedService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map;

/**
 * @author wwh
 * @date 2023/11/1
 *
 */
@Service
@Slf4j
public class RecommendedServiceImpl implements RecommendedService {

    @Resource
    private MarkMapper markMapper;
    @Resource
    private SimilarityMapper similarityMapper;


    @Override
    public Map<Integer, MarkVo> getRecommend() {
        return markMapper.select();
    }

    
    @Override
    public List<Integer> getVideoTypeByUserId(Integer userId) {
        return markMapper.selectByUserId(userId);
    }

    @Override
    public void getSimilar(Map<Integer, MarkVo> data) {
        // 示例数据
        Map<String, Map<String, Double>> similarityMatrix = new HashMap<>();
        for (Integer user1 : data.keySet()) {
            for (Integer user2 : data.keySet()) {
                if (!user1.equals(user2)) {
                    double jaccardSimilarity = calculateJaccardSimilarity(data.get(user1).markVoList, data.get(user2).markVoList);
                    similarityMatrix.computeIfAbsent(String.valueOf(user1), k -> new HashMap<>()).put(String.valueOf(user2), jaccardSimilarity);
                }
            }
        }

        // 打印相似度矩阵
        for (Map.Entry<String, Map<String, Double>> entry : similarityMatrix.entrySet()) {
            String user1 = entry.getKey();
            Map<String, Double> similarUsers = entry.getValue();
            for (Map.Entry<String, Double> similarityEntry : similarUsers.entrySet()) {
                String user2 = similarityEntry.getKey();
                double similarity = similarityEntry.getValue();
                if(similarity>0.5){
                    Similarity similarity1 = new Similarity();
                    similarity1.setUserOne(Integer.parseInt(user1));
                    similarity1.setUserTwo(Integer.parseInt(user2));
                    similarity1.setSimilarity(similarity);
                    similarityMapper.insert(similarity1);
                }

                log.info(user1 + " and " + user2 + " Jaccard Similarity: " + similarity);
            }
        }
    }

    @Override
    public List<Similarity> getSimilarityByUser(Integer userId) {
        return similarityMapper.selectList(new QueryWrapper<Similarity>().eq("user_one", userId));
    }

    public static double calculateJaccardSimilarity(List<String> set1, List<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2); // 计算交集
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2); // 计算并集

        if (union.isEmpty()) {
            return 0.0; // 避免除零错误
        } else {
            return (double) intersection.size() / union.size();
        }
    }
}
