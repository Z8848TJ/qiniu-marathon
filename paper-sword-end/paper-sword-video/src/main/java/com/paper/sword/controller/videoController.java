package com.paper.sword.controller;

import com.paper.sword.common.mapper.MarkMapper;
import com.paper.sword.common.vo.MarkBo;
import com.paper.sword.common.vo.MarkVo;
import com.paper.sword.common.vo.Result;
import com.paper.sword.video.RecommendedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wwh
 * @date 2023/11/1
 */
@RestController()
@RequestMapping("/video")
@Slf4j
public class videoController {
    @Autowired
    public RecommendedService recommendedService;
    @RequestMapping("/mark")
    public Result get(){
        Map<Integer, MarkVo> recommend = recommendedService.getRecommend();
        recommendedService.getSimilar(recommend);
        return Result.success().data(recommend);
    }
}
