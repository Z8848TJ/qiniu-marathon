package com.paper.sword.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paper.sword.user.entity.LikeViodeVo;
import com.paper.sword.user.entity.Like;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Mapper
public interface LikeMapper extends BaseMapper<Like> {

    public List<LikeViodeVo> getLikeVideo(@Param("userId") String userId);

    public List<LikeViodeVo> getCollectVideo(@Param("userId") String userId);
}
