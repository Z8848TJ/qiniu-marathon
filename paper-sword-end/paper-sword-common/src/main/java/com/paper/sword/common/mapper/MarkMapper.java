package com.paper.sword.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paper.sword.common.vo.MarkBo;
import com.paper.sword.common.vo.MarkVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wwh
 * @date 2023/10/28
 */
@Mapper
public interface MarkMapper extends BaseMapper<MarkBo> {

    Integer selectByUserIdAndType(@Param("userId") Integer userId,@Param("type") Integer type);

    @MapKey("userId")
    Map<Integer, MarkVo> select();


    List<Integer> selectByUserId(@Param("userId") Integer userId);

    void updateByUserAndType(MarkBo markVo);
}
