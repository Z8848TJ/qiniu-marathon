package com.paper.sword;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class VideoFrameExtractor {
    public static String getFrame(String videoPath,String outputDir) {
        int frameCount = getVideoFrameCount(videoPath);
        int count = frameCount / 10;
        String outputDirPath = outputDir;
        File outputDirFile = new File(outputDirPath);
        outputDirFile.mkdirs();
        extractFrames(videoPath, outputDirPath,count);

        return outputDir;
    }

    private static void extractFrames(String videoPath, String outputDir,int framesPerSecond) {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
        try {
            grabber.start();
            Frame frame;
            int frameNumber = 0;
            while ((frame = grabber.grab()) != null) {
                if (frameNumber % framesPerSecond == 0) {
                    Date date = new Date();
                    String frameFileName = String.format("%s/frame%05d.jpg", outputDir,date.getTime());

                    // 将 OpenCV Mat 转换为 Java BufferedImage
                    org.bytedeco.javacv.Java2DFrameConverter frameConverter = new org.bytedeco.javacv.Java2DFrameConverter();
                    BufferedImage bufferedImage = frameConverter.getBufferedImage(frame, 1.0);

                    // 保存图像使用 ImageIO
                    try {
                        if(bufferedImage != null){
                            File outputImage = new File(frameFileName);
                            ImageIO.write(bufferedImage, "jpg", outputImage);
                            System.out.println(frameFileName);
                            System.out.println("图像保存成功");
                        }
                    } catch (IOException e) {
                        System.out.println("图像保存失败: " + e.getMessage());
                    }
                }
                frameNumber++;
            }
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getVideoFrameCount(String videoPath) {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
        grabber.setAudioChannels(0);
        try {
            grabber.start();
            int frameCount = 0;

            while (grabber.grab() != null) {
                frameCount++;
            }

            grabber.stop();
            return frameCount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
