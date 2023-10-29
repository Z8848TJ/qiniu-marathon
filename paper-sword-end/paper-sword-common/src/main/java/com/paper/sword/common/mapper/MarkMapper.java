package com.paper.sword.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paper.sword.common.vo.MarkVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wwh
 * @date 2023/10/28
 */
@Mapper
public interface MarkMapper extends BaseMapper<MarkVo> {

    Integer selectByUserIdAndType(@Param("userId") Integer userId,@Param("type") Integer type);

}
