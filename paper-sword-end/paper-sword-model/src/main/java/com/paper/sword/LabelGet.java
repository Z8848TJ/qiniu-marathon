package com.paper.sword;
import com.paper.sword.common.vo.fileVo;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author wwh
 * @date 2023/10/29
 */
@Slf4j
public class LabelGet {
    
    public static fileVo getLabelList(String scriptPath, String videoPath, String outputDir,String url) {
        fileVo fileVo = new fileVo();
        String line;
        StringBuilder ans = new StringBuilder();
        try {

            String[] cmd = {url, scriptPath,outputDir};
            Process pr = Runtime.getRuntime().exec(cmd);
            String arg1 = VideoFrameExtractor.getFrame(videoPath,outputDir);
            
            // 从进程的输出流中读取结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                ans.append(line);
            }

            fileVo.setImagePath(arg1);
            fileVo.setType(ans.toString());

            // 等待进程执行完毕
            int exitCode = pr.waitFor();
            log.info("Python脚本执行完毕，退出码：{}", exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return fileVo;
    }
        
}
