# -*- coding: utf-8 -*-
import torch
import torch.nn as nn
from torchvision import models, transforms
from PIL import Image
import torch.nn.functional as F
import sys
import os


if __name__ == '__main__':
    if len(sys.argv) < 1:
        print("Usage: python your_script.py argument1 argument2 ...")
        sys.exit(1)

    arg1 = "D:\demo"
    sys.stdout.reconfigure(encoding='utf-8')

    custom_class_to_idx = {
        '云海': 0,
        '动漫': 1,
        '唯美': 2,
        '夜景': 3,
        '游戏': 4,
        '美女': 5,
        '美食': 6,
        '语录': 7,
        '雨景': 8,
        '风景': 9
    }

    # 创建模型
    model = nn.Sequential(
        nn.Conv2d(3, 64, kernel_size=3, padding=1),
        nn.ReLU(),
        nn.MaxPool2d(2),
        nn.Conv2d(64, 128, kernel_size=3, padding=1),
        nn.ReLU(),
        nn.MaxPool2d(2),
        nn.Flatten(),
        nn.Linear(128 * 56 * 56, 256),
        nn.ReLU(),
        nn.Linear(256, 10)
    )

    model.load_state_dict(torch.load('D:/frames/model/model.pt'))
    model.eval()

    # 图像处理函数
    def preprocess_image(image_path):
        transform = transforms.Compose([
            transforms.Resize(256),
            transforms.CenterCrop(224),
            transforms.ToTensor(),
            transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
        ])
        image = Image.open(image_path)
        image = transform(image)
        image = image.unsqueeze(0)
        return image


    my_list = []
    for root,dirs,files in os.walk(arg1):
        for file in files:
            # 加载图像并进行预测
            image_path = os.path.join(root, file)
            image = preprocess_image(image_path)

            with torch.no_grad():
                outputs = model(image)

            # 解析分类结果
            probabilities = F.softmax(outputs, dim=1)

            top2_probabilities, top2_labels = torch.topk(probabilities, k=1, dim=1)

            # top2_labels 包含每个输入样本的两个最高概率的类别标签
            # top2_probabilities 包含相应的两个最高概率值

            top2_labels_numpy = top2_labels.numpy()

            for i in range(len(top2_labels_numpy)):
                for j in range(len(top2_labels_numpy[i])):
                    # 定义一个数字类别索引
                    class_idx = top2_labels_numpy[i][j]
                    print(class_idx)
                    print("概率为")
                    print(top2_probabilities)
                    if top2_probabilities[i][j] < 0.9:
                        break;

                    # 通过数字索引获取类别标签
                    label = None
                    for class_name, idx in custom_class_to_idx.items():
                        if idx == class_idx:
                            label = class_name
                            break
                    # 输出类别标签
                    if label is not None:
                        if label not in my_list:
                            my_list.append(label)
                    else:
                        continue;
    if len(my_list) == 0:
        print("无法匹配标签")
    else:
        print(my_list)