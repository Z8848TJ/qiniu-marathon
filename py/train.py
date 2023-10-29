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

    # 定义损失函数和优化器
    criterion = nn.CrossEntropyLoss()
    optimizer = optim.SGD(model.parameters(), lr=0.001, momentum=0.9)

    # 训练模型
    num_epochs = 10
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

    model.to(device)

    for epoch in range(num_epochs):
        model.train()
        running_loss = 0.0

        for images, labels in train_loader:
            images, labels = images.to(device), labels.to(device)
            print("Images shape:", images.shape)
            # 打印 labels 的内容
            print("Labels:", labels)
            optimizer.zero_grad()
            outputs = model(images)
            loss = criterion(outputs, labels)
            loss.backward()
            optimizer.step()

            running_loss += loss.item()

        print(f'Epoch [{epoch + 1}/{num_epochs}] Loss: {running_loss / len(train_loader)}')

    print('Finished Training')

    # 保存训练好的模型
    torch.save(model.state_dict(), 'D:/frames/model/model.pt')
