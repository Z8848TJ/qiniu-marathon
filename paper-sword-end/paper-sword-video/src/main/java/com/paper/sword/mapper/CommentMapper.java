package com.paper.sword.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paper.sword.video.entit.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wwh
 * @date 2023/10/26
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
