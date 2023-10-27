package com.paper.sword.controller.user;

import com.paper.sword.common.vo.Result;
import com.paper.sword.common.vo.UserHolder;
import com.paper.sword.common.vo.UserVO;
import com.paper.sword.user.MessageService;
import com.paper.sword.user.entity.Video;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: zzh
 * @description: 消息推送
 */
@RequestMapping("/message")
@RestController
public class MessageController {
    
    @Reference
    private MessageService messageService;

    /**
     * 向用户推送消息
     * @return 未读消息
     */
    @GetMapping("/push")
    public Result pushInteractMessage() {
        UserVO user = UserHolder.getUser();
        String userId = user.getUserId();
        
        List<String> info = messageService.pushInteractMessage(userId);
        
        return Result.success().data(info);
    }

    /**
     * 用户阅读消息
     * @param ids 已读消息 id
     */
    @GetMapping("/read")
    public Result readMessage(String ids) {
        messageService.updateStatusByIds(ids);
        
        return Result.success();
    }
    
}
