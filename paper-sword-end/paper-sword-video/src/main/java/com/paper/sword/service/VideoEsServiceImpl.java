package com.paper.sword.service;

import com.paper.sword.common.vo.EsVideo;
import com.paper.sword.video.VideoService;
import com.paper.sword.common.entity.Video;
import com.paper.sword.video.VideoEsService;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wwh
 * @date 2023/10/30
 */
@Service
public class VideoEsServiceImpl implements VideoEsService {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Autowired
    private VideoService videoService;


    @Override
    public void saveEsVideo(EsVideo esVideo) {
        elasticsearchTemplate.save(esVideo);
    }

    @Override
    public List<Video> getEsVideoByType(String type) {
        List<Video> esVideoList = new ArrayList<>();
        SortBuilder<?> sort = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        String[] split = type.split(",");
        BoolQueryBuilder should = QueryBuilders.boolQuery();
        for (String s : split) {
            should.should(QueryBuilders.matchQuery("videoType", s));
        }
        NativeSearchQueryBuilder nativeSearchQuery = new NativeSearchQueryBuilder();
        nativeSearchQuery.withQuery(should);
        nativeSearchQuery.withSort(sort);
        SearchHits<EsVideo> search = elasticsearchTemplate.search(nativeSearchQuery.build(), EsVideo.class);
        for (SearchHit<EsVideo> esVideoSearchHit : search) {
            String id = esVideoSearchHit.getContent().getVideoId();
            Video video = videoService.getById(id);
            esVideoList.add(video);
        }
        
        // 补全视频列表
        if(esVideoList.size() < 20) {
            videoService.completeVideo(esVideoList);
        }
        
        return esVideoList;
    }

    @Override
    public List<String> getEsVideo(String keyWord) {
        ArrayList<String> EsVideoList = new ArrayList<>();
        SortBuilder<?> sort = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        BoolQueryBuilder should = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("description", keyWord));
        NativeSearchQueryBuilder nativeSearchQuery = new NativeSearchQueryBuilder();
        nativeSearchQuery.withQuery(should);
        nativeSearchQuery.withSort(sort);
        SearchHits<EsVideo> search = elasticsearchTemplate.search(nativeSearchQuery.build(), EsVideo.class);
        for (SearchHit<EsVideo> esVideoSearchHit : search) {
            String id = esVideoSearchHit.getContent().getVideoId();
            EsVideoList.add(id);
        }
        return EsVideoList;
    }
}
