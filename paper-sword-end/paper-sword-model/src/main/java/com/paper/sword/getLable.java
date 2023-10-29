package com.paper.sword;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author wwh
 * @date 2023/10/29
 */
public class getLable {
        public static void main(String[] args) {
            try {
                // 定义Python脚本的命令和参数
                String pythonScript = "python";
                String scriptPath = "C:\\Users\\86151\\PycharmProjects\\pythonProject2\\demo.py";
                String arg1 = VideoFrameExtractor.getFrame();

                // 创建ProcessBuilder并设置命令和参数
                ProcessBuilder processBuilder = new ProcessBuilder(pythonScript, scriptPath, arg1);

                // 启动进程
                Process process = processBuilder.start();

                // 从进程的输出流中读取结果
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                // 等待进程执行完毕
                int exitCode = process.waitFor();
                System.out.println("Python脚本执行完毕，退出码：" + exitCode);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
