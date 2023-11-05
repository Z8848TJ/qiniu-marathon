package com.paper.sword.common.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wwh
 * @date 2023/10/26
 */
@Data
public class LikeVideoVo {
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    public String id;

    public String userId;

    public String videoId;

    public Integer videoType;

    public String videoUrl;

    public String like;

    public String collect;

    public String title;

    public String description;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public Date createTime;
}
