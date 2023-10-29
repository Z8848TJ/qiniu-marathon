package com.paper.sword;

import ai.djl.Application;
import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.Classifications.Classification;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.CenterCrop;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TrainImageClassificationModel {
    public static void main(String[] args) {
        String datasetPath = "D:\\frames\\train"; // 替换为你的数据集路径
        int epochs = 10; // 设置训练轮数
        int batchSize = 32; // 设置每批次的样本数量

        try {

            // 创建 Criteria 对象
            Criteria<Image, Classifications> criteria = Criteria.builder()
                    .setTypes(Image.class, Classifications.class)
                    .optModelName("D:\\frames\\model\\model.pt") // 指定要加载的模型名称
                    .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                    .build();
            // 加载预训练的模型
            Model model = ModelZoo.loadModel(criteria);
            Pipeline pipeline = new Pipeline();
            pipeline.add(new Resize(256))
                    .add(new CenterCrop(224, 224))
                    .add(new ToTensor())
                    .add(new Normalize(
                            new float[]{0.485f, 0.456f, 0.406f},
                            new float[]{0.229f, 0.224f, 0.225f}));

            // 创建 Translator
            Translator<Image, Classifications> translator = ImageClassificationTranslator.builder()
                    .setPipeline(pipeline)
                    .optFlag(Image.Flag.COLOR) // 图像颜色标志（如果有必要的话）
                    .optSynsetArtifactName("D:\\frames\\synset.txt") // 标签文件（如果有必要的话）
                    .optApplySoftmax(true) // 应用 softmax 函数
                    .build();

            // 使用模型进行推理
            try (Predictor<Image, Classifications> predictor = model.newPredictor(translator)) {
                // 在这里执行图像分类推理
                // 遍历数据集并用模型进行分类
                Path datasetDir = Paths.get(datasetPath);
                Files.list(datasetDir).forEach(categoryDir -> {
                    if (Files.isDirectory(categoryDir)) {
                        String category = categoryDir.getFileName().toString();
                        System.out.println("Category: " + category);
                        try {
                            Files.list(categoryDir).forEach(cDir->{
                                if(Files.isDirectory(cDir)){
                                    String c = cDir.getFileName().toString();
                                    System.out.println("children: " + c);
                                }
                                try {
                                    Files.list(cDir).forEach(imageFile -> {
                                        try {
                                            BufferedImage bufferedImage = ImageIO.read(imageFile.toFile());
                                            Image image = ImageFactory.getInstance().fromImage(bufferedImage);
                                            Classifications predictions = predictor.predict(image);

                                            // 打印分类结果
                                            List<Classification> items = predictions.items();
                                            Classification topPrediction = items.get(0);
                                            System.out.println("Image: " + imageFile.getFileName());
                                            System.out.println("Predicted Class: " + topPrediction.getClassName());
                                            System.out.println("Probability: " + topPrediction.getProbability());
                                        } catch (IOException | TranslateException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (IOException | ModelException e) {
            e.printStackTrace();
        }
    }
}