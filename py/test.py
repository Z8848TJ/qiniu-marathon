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

    # 定义损失函数
    criterion = nn.CrossEntropyLoss()

    # 测试模型
    correct = 0
    total = 0
    loss = 0

    with torch.no_grad():
        for images, labels in train_loader:
            outputs = model(images)
            _, predicted = torch.max(outputs, 1)
            total += labels.size(0)
            correct += (predicted == labels).sum().item()
            loss += criterion(outputs, labels).item()

    accuracy = 100 * correct / total
    print(f'Test Accuracy: {accuracy:.2f}%')
    print(f'Average Loss: {loss / len(train_loader):.4f}')
