package com.paper.sword.common.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author wwh
 * @date 2023/11/2
 */
@Data
@TableName("t_similarity")
public class Similarity {
    public Integer userOne;

    public Integer userTwo;

    public Double similarity;
}
