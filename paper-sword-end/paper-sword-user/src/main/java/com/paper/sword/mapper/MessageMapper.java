package com.paper.sword.mapper;

import com.paper.sword.user.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


public interface MessageMapper extends BaseMapper<Message> {

    void updateStatusByIds(String ids);
}




