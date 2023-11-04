package com.paper.sword.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paper.sword.common.entity.Video;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Mapper
public interface VideoMapper extends BaseMapper<Video> {
    List<Video> selectUrlList(String join);
}
