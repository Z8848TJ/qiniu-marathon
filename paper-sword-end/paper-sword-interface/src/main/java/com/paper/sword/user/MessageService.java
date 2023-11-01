package com.paper.sword.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.paper.sword.common.vo.MessageVO;
import com.paper.sword.user.entity.Message;

import java.util.List;

/**
 * @author: zzh
 * @description:
 */
public interface MessageService extends IService<Message> {


    List<MessageVO> pushInteractMessage(Integer userId);

    void updateStatusByIds(String ids);
}
