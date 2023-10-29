import torch
import torch.nn as nn
import torch.optim as optim
from data_loader import train_loader

# 定义模型架构
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
optimizer = optim.SGD(model.parameters(), lr=0.001)

# 加载之前训练的模型权重
checkpoint = torch.load('D:/frames/model/model.pt')
model.load_state_dict(checkpoint)

num_epochs = 15

# 继续训练模型
for epoch in range(num_epochs):
    for inputs, labels in train_loader:
        optimizer.zero_grad()
        outputs = model(inputs)
        loss = criterion(outputs, labels)
        loss.backward()
        optimizer.step()

# 保存继续训练后的模型
torch.save(model.state_dict(), 'D:/frames/model/remodel.pt')
