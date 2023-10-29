package com.paper.sword;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class JavaInferenceExample {
    public static void main(String[] args) {
        Module mod = Module.load("D:\\frames\\model\\scripted.pt");
        float[] inputData = loadImage();
        Tensor inputTensor = Tensor.fromBlob(inputData, new long[]{1, inputData.length});
        IValue result = mod.forward(IValue.from(inputTensor), IValue.from(3.0));
        Tensor output = result.toTensor();
        System.out.println("shape: " + Arrays.toString(output.shape()));
        System.out.println("data: " + Arrays.toString(output.getDataAsFloatArray()));

        // Workaround for https://github.com/facebookincubator/fbjni/issues/25
        System.exit(0);
    }

    public static float[] loadImage() {
        // 假设你要加载一张图像，并将其像素值存储在 inputData 中
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("D:\\frames\\动漫\\0\\frame00400.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        float[] inputData = new float[imageWidth * imageHeight * 3];  // 3表示RGB通道

        int index = 0;
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int color = image.getRGB(x, y);
                // 解析RGB通道，并将像素值归一化为[0, 1]范围的浮点数
                float r = ((color >> 16) & 0xFF) / 255.0f;
                float g = ((color >> 8) & 0xFF) / 255.0f;
                float b = (color & 0xFF) / 255.0f;
                inputData[index++] = r;
                inputData[index++] = g;
                inputData[index++] = b;
            }
        }
        return inputData;
    }

}
