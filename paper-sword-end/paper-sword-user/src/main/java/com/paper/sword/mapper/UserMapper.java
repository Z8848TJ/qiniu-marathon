package com.paper.sword.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paper.sword.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
