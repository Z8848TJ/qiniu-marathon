import torch
import torch
import torch.nn as nn
import torch.optim as optim

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

    # 导出模型为TorchScript格式
    scripted_model = torch.jit.script(model)
    scripted_model.save('D:/frames/model/scripted.pt')