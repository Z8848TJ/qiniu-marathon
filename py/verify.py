import os
import torch
import torch.nn as nn
import torch.optim as optim
from data_loader import train_loader

if __name__ == '__main__':

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

    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

    model.to(device)

    correct = 0
    total = 0
    with torch.no_grad():
        for images, labels in train_loader:
            images, labels = images.to(device), labels.to(device)  # 如果使用GPU，将数据移至GPU
            outputs = model(images)
            _, predicted = torch.max(outputs, 1)
            total += labels.size(0)
            correct += (predicted == labels).sum().item()

    accuracy = 100 * correct / total
    print('Validation Accuracy: {}%'.format(accuracy))