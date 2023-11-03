package com.paper.sword.common.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.*;

/**
 * @author wwh
 * @date 2023/10/28
 */
@Data
@TableName("t_mark")
public class MarkVo {
    public Integer userId;

    public List<String> markVoList;
}
