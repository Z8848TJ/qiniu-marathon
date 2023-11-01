package com.paper.sword.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paper.sword.user.entity.Like;
import com.paper.sword.common.vo.LikeVideoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Mapper
public interface LikeMapper extends BaseMapper<Like> {

    List<LikeVideoVo> getLikeVideo(@Param("userId") Integer userId);

    List<LikeVideoVo> getCollectVideo(@Param("userId") Integer userId);
}
