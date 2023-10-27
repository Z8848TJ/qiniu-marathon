package com.paper.sword.controller.video;

import com.paper.sword.common.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wwh
 * @date 2023/10/26
 */
@RequestMapping("/video")
@RestController
public class VideoController {

    @RequestMapping(value = "/token",method = RequestMethod.GET)
    public Result getUpdateToken(){
        String accessKey = "access key";
        String secretKey = "secret key";
        String bucket = "bucket name";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        System.out.println(upToken);
        return Result.success();
    }

}
