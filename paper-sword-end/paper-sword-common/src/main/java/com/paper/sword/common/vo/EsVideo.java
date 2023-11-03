package com.paper.sword.common.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * @author wwh
 * @date 2023/10/30
 */
@Data
@Document(indexName = "esvideo", createIndex = true)
public class EsVideo implements Serializable {

    @Id
    public String id;

    public String videoType;

    public String title;

    public String description;

    public Date createTime;

}
