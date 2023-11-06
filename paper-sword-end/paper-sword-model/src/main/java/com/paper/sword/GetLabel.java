package com.paper.sword;
import com.paper.sword.common.vo.fileVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author wwh
 * @date 2023/10/29
 */
public class GetLabel {
        public static fileVo getLabel(String scriptPath,String videoPath,String outputDir) {
            fileVo fileVo = new fileVo();
            String line = "";
            String ans = "";
            try {
                // 定义Python脚本的命令和参数
                String pythonScript = "python3";
                String arg1 = VideoFrameExtractor.getFrame(videoPath,outputDir);
                // 创建ProcessBuilder并设置命令和参数
                ProcessBuilder processBuilder = new ProcessBuilder(pythonScript, scriptPath, outputDir);

                // 启动进程
                Process process = processBuilder.start();

                // 从进程的输出流中读取结果
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    ans+=line;
                }

                fileVo.setImagePath(arg1);
                fileVo.setType(ans);

                // 等待进程执行完毕
                int exitCode = process.waitFor();
                System.out.println("Python脚本执行完毕，退出码：" + exitCode);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            return fileVo;
        }
}
