package com.paper.sword.controller.user;

import com.paper.sword.common.vo.Result;
import com.paper.sword.user.FollowService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/follow")
@RestController
public class FollowController {
    
    @Reference
    private FollowService followService;

    /**
     * 关注或取关
     * @param followUserId 关注者 ID
     * @param isFollow 关注 or 取关
     */
    @PostMapping("/{followUserId}/{isFollow}")
    public Result follow(@PathVariable String followUserId, @PathVariable Boolean isFollow) {
        followService.follow(followUserId, isFollow);
        
        return Result.success();
    }

    /**
     * 查看是否关注
     * @param followUserId 关注者 ID
     * @return 是否关注 true or false
     */
    @GetMapping("/or/not/{{followUserId}}")
    public Result isFollow(@PathVariable String followUserId) {
        boolean flag = followService.isFollow(followUserId);
        return Result.success().data(flag);
    }
    
}
