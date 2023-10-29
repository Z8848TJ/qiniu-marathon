import torchvision.transforms as transforms
from torchvision.datasets import ImageFolder
from torch.utils.data import DataLoader
import os
from torchvision import transforms
from torchvision.datasets import ImageFolder
from torch.utils.data import DataLoader, Dataset
from PIL import Image

class CustomImageFolderDataset(Dataset):
    def __init__(self, root, transform=None, class_to_idx=None):
        self.root = root
        self.transform = transform
        self.class_to_idx = class_to_idx
        self.samples = []

        # 构建 samples 列表，包含图像文件路径和类别标签
        for class_name in os.listdir(self.root):
            class_dir = os.path.join(self.root, class_name)
            if os.path.isdir(class_dir):
                class_idx = class_to_idx[class_name]
                for img_name in os.listdir(class_dir):
                    img_path = os.path.join(class_dir, img_name)
                    self.samples.append((img_path, class_idx))

    def __len__(self):
        return len(self.samples)

    def __getitem__(self, index):
        img_path, class_idx = self.samples[index]
        image = Image.open(img_path).convert('RGB')

        if self.transform:
            image = self.transform(image)

        return image, class_idx

custom_class_to_idx = {
    '云海':0,
    '动漫':1,
    '唯美':2,
    '夜景':3,
    '游戏':4,
    '美女':5,
    '美食':6,
    '语录':7,
    '雨景':8,
    '风景':9
}

# 数据预处理和增强操作
transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
])

# 数据集目录
data_dir = 'D:/frames/retrain'

# 创建数据集
custom_dataset = CustomImageFolderDataset(root=data_dir, transform=transform, class_to_idx=custom_class_to_idx)

# 创建数据加载器
batch_size = 32
train_loader = DataLoader(custom_dataset, batch_size=batch_size, shuffle=True)