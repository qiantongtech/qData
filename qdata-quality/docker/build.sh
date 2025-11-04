#!/bin/bash
# 删除旧镜像
docker rmi qiantong/qdata-quality:1.0.7

# 重新构建镜像
docker build --no-cache \
  --build-arg http_proxy=http://192.168.0.112:10809 \
  --build-arg https_proxy=http://192.168.0.112:10809 \
  -t qiantong/qdata-quality:1.0.7 .

# 启动新容器
docker run -d \
  --name qdata-quality \
  -p 8083:8083 \
  qiantong/qdata-quality:1.0.7
