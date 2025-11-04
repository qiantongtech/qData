#!/bin/bash
# 删除旧镜像
docker rmi qiantong/qdata:1.0.7

# 重新构建镜像 替换成你的代理或者自行更改国内源
docker build --no-cache \
  --build-arg http_proxy=http://192.168.0.112:10809 \
  --build-arg https_proxy=http://192.168.0.112:10809 \
  -t qiantong/qdata:1.0.7 .

# 启动新容器
docker run -d \
  --name qdata \
  -p 8080:8080 \
  qiantong/qdata:1.0.7
