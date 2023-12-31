package com.paper.sword.mapper;

import com.paper.sword.user.entity.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paper.sword.user.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

    List<User> selectFollowList(Integer userId);
}




