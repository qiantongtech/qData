#!/bin/bash

# 创建 builder（关键一步）
docker buildx create \
  --name qdata-builder \
  --driver docker-container \
  --use

# 启动 builder（加载 QEMU 等）
docker buildx inspect --bootstrap

# 构建 AMD64（x86_64）版本
cd /mnt/c/Users/Ming/Desktop/qData/qdata-server-ce # 路径改成你自己的路径

docker buildx build \
  --platform linux/amd64 \
  --no-cache \
  -t crpi-kf13onfj0v8f6jax.cn-shanghai.personal.cr.aliyuncs.com/qiantongkeji/qdata-server-ce:1.1.0 \
  --file=docker/Dockerfile \
  --load \
  /mnt/c/Users/Ming/Desktop/qData/qdata-server-ce # 上下文路径改成你自己的路径

# 构建 ARM64（适配鲲鹏、飞腾、树莓派等 ARM 服务器）
docker buildx build \
  --platform linux/arm64 \
  --no-cache \
  -t crpi-kf13onfj0v8f6jax.cn-shanghai.personal.cr.aliyuncs.com/qiantongkeji/qdata-server-ce:1.1.0 \
  --file=docker/Dockerfile \
  --load \
  /mnt/c/Users/Ming/Desktop/qData/qdata-server-ce # 上下文路径改成你自己的路径

# 检查是否支持 ARM64
docker inspect crpi-kf13onfj0v8f6jax.cn-shanghai.personal.cr.aliyuncs.com/qiantongkeji/qdata-server-ce:1.1.0 --format '{{.Architecture}}'

# 删掉之前建的 builder（可选但建议，保持干净）
docker buildx rm qdata-builder

# 启动新容器
docker run -d \
  --name qdata-server-ce \
  -p 8080:8080 \
  crpi-kf13onfj0v8f6jax.cn-shanghai.personal.cr.aliyuncs.com/qiantongkeji/qdata-server-ce:1.1.0
