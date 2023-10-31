package com.paper.sword;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author wwh
 * @date 2023/10/29
 */
public class getLable {
        public static String getLable(String scriptPath,String videoPath,String outputDir) {
            String line = "";
            try {
                // 定义Python脚本的命令和参数
                String pythonScript = "python";
                String arg1 = VideoFrameExtractor.getFrame(videoPath,outputDir);
                // 创建ProcessBuilder并设置命令和参数
                ProcessBuilder processBuilder = new ProcessBuilder(pythonScript, scriptPath, arg1);

                // 启动进程
                Process process = processBuilder.start();

                // 从进程的输出流中读取结果
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                // 等待进程执行完毕
                int exitCode = process.waitFor();
                System.out.println("Python脚本执行完毕，退出码：" + exitCode);

                File folder = new File(outputDir);
                File[] files = folder.listFiles();
                for (File file : files) {
                    if(file.isFile()){
                        file.delete();
                    }
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            return line;
        }
    }
