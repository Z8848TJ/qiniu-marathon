package com.paper.sword.controller.user;

import com.paper.sword.common.vo.Result;
import com.paper.sword.user.FollowService;
import com.paper.sword.user.entity.User;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/opt")
    public Result follow(@RequestParam Integer followUserId, @RequestParam Boolean isFollow) {
        followService.follow(followUserId, isFollow);

        return Result.success();
    }

    /**
     * 查看是否关注
     * @param followUserId 关注者 ID
     * @return 是否关注 true or false
     */
    @GetMapping("/or/not")
    public Result isFollow(@RequestParam Integer followUserId) {
        boolean flag = followService.isFollow(followUserId);
        return Result.success().data(flag);
    }

    @GetMapping("/list")
    public Result followList(@RequestParam Integer userId) {
        List<User> res = followService.followList(userId);

        return Result.success().data(res);
    }
    

}
