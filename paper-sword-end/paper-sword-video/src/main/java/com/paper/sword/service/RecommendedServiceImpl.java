package com.paper.sword.service;

import com.paper.sword.common.mapper.MarkMapper;
import com.paper.sword.video.RecommendedService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author wwh
 * @date 2023/11/1
 *
 */
@Service
public class RecommendedServiceImpl implements RecommendedService {

    @Autowired
    private MarkMapper markMapper;


    @Override
    public Map<Integer, Integer> getRecommend(Integer userId) {
        return markMapper.selectByUserId(1);
    }
}
