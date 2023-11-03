package com.paper.sword.common.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wwh
 * @date 2023/11/2
 */
@Data
@TableName("t_similarity")
public class Similarity implements Serializable {
    public Integer userOne;

    public Integer userTwo;

    public Double similarity;
}
