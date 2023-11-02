package com.paper.sword.common.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author wwh
 * @date 2023/10/28
 */
@Data
@TableName("t_mark")
public class MarkBo {

    public Integer userId;

    public Integer videoType;

    public Integer mark;

    public MarkBo(int videoType, int mark) {
        this.videoType = videoType;
        this.mark = mark;
    }

    public MarkBo() {
    }
}
