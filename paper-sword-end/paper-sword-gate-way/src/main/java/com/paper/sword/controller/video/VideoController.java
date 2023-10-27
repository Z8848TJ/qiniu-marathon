package com.paper.sword.controller.video;

import com.paper.sword.config.QiniuConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    QiniuConfig qiniuConfig;

    @GetMapping("/test")
    public void test() {
        System.out.println(qiniuConfig.getAccessKey());
    }
}
