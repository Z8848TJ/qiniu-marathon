package com.paper.sword.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paper.sword.user.entity.Comment;
import com.paper.sword.common.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wwh
 * @date 2023/10/26
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentVo> selectCommentList(@Param("videoId") String videoId, 
                                      @Param("begin") Integer begin);
}
