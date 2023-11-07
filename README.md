# 搞子剑视频

### 程序运行

前端项目运行环境：

- Node.Js
- 包管理工具（yarn 或 npm）

后端项目运行环境：

- JDK 1.8及以上
- MySQL5.7及以上
- Maven
- Redis
- ElasticSearch
- Kafka 3.0及以上
- Zookeeper

#### 拉取项目代码

```
git clone git@github.com:Z8848TJ/qiniu-marathon.git
```

#### 下载前端依赖

```
npm install
# 或
yarn add
```

#### 修改后端配置文件

gate-way 模块：

- label.properties
  - label.scriptPath：修改 py 目录下 demo.py 文件位置为自己的文件地址
  - label.outputDir：修改视频分析后的图片的输出位置
- qiniu.properties
  - access-key：七牛云 access-key；
  - secret-key：七牛云 secret-key；
  - qiniu.domain：七牛云上传文件回调通知地址；

user 模块与 video 模块：

修改 application.yml 下的 Redis，ElasticSearch，Kafka，Dubbo，Zookeeper 等组件的相关 IP 与端口。

项目演示地址：http://49.234.3.91:8070/

项目录制视频地址：https://www.bilibili.com/video/BV1684y1Q7RF/