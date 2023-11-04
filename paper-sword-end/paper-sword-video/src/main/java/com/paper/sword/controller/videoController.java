package com.paper.sword.controller;

import com.paper.sword.common.entity.Video;
import com.paper.sword.common.mapper.MarkMapper;
import com.paper.sword.common.vo.EsVideo;
import com.paper.sword.common.vo.MarkBo;
import com.paper.sword.common.vo.MarkVo;
import com.paper.sword.common.vo.Result;
import com.paper.sword.video.RecommendedService;
import com.paper.sword.video.VideoEsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    public VideoEsService videoEsService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;
    @RequestMapping("/mark")
    public Result get(){

        //创建索引
		IndexOperations indexOperations = elasticsearchTemplate.indexOps(EsVideo.class);
		if(indexOperations.exists()){
			System.out.println("exits");
		}
		Map<String, Object> settings = new HashMap<>();
		settings.put("index.number_of_shards", 5);
		settings.put("index.number_of_replicas", 2);
		indexOperations.create(settings);
		Document mapping = indexOperations.createMapping(EsVideo.class);
		indexOperations.putMapping(mapping);
        Map<Integer, MarkVo> recommend = recommendedService.getRecommend();
        recommendedService.getSimilar(recommend);
        return Result.success().data(recommend);
    }


    @RequestMapping("/recommend")
    public Result recommend(String keyword){

        List<String> ceshi = videoEsService.getEsVideo(keyword);
        return Result.success().data(ceshi);
    }
}
