package com.paper.sword.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paper.sword.common.entity.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wwh
 * @date 2023/11/4
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict> {
    Dict getValueByKey(@Param("key") Integer key);
    
    List<Dict> getVideoType();
}
