package com.paper.sword.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paper.sword.common.vo.MarkBo;
import com.paper.sword.common.vo.MarkVo;
import com.paper.sword.common.vo.Similarity;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author wwh
 * @date 2023/10/28
 */
@Mapper
public interface SimilarityMapper extends BaseMapper<Similarity> {


}
