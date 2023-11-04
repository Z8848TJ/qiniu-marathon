package com.paper.sword.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Data
@TableName("t_video")
public class Video implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    public String id;

    public Integer userId;

    public String videoType;

    public String videoUrl;


    public String username;

    public String cover;

    public String description;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public Date createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Video)) return false;

        Video video = (Video) o;

        return new EqualsBuilder().append(getId(), video.getId()).append(getUserId(), video.getUserId()).append(getVideoType(), video.getVideoType()).append(getVideoUrl(), video.getVideoUrl()).append(getUsername(), video.getUsername()).append(getCover(), video.getCover()).append(getDescription(), video.getDescription()).append(getCreateTime(), video.getCreateTime()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).append(getUserId()).append(getVideoType()).append(getVideoUrl()).append(getUsername()).append(getCover()).append(getDescription()).append(getCreateTime()).toHashCode();
    }
}
