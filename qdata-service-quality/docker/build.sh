#!/bin/bash
# Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
#
# This file is part of qData Data Middle Platform (Open Source Edition).
#
# qData is licensed under Apache License 2.0 with additional qData terms.
# You may use qData for commercial purposes, but you may not remove, hide,
# modify, or replace the qData logo, copyright notices, license notices,
# or attribution information without a separate commercial license.
#
# White-label use, OEM distribution, rebranding, or presenting qData as
# another product requires separate commercial authorization from
# Jiangsu Qiantong Technology Co., Ltd.
#
# Business License: https://docs.qdata.tech/business/policy.html
# See the LICENSE file in the project root for full license information.

# Create builder (key step)
docker buildx create \
  --name qdata-builder \
  --driver docker-container \
  --use

# Start builder (load QEMU, etc.)
docker buildx inspect --bootstrap

# Build AMD64 (x86_64) version
cd /mnt/c/Users/Ming/Desktop/qData/qdata-service-quality-ce # Change the path to your own path

docker buildx build \
  --platform linux/amd64 \
  --no-cache \
  -t crpi-kf13onfj0v8f6jax.cn-shanghai.personal.cr.aliyuncs.com/qiantongkeji/qdata-service-quality-ce:1.6.9 \
  --file=docker/Dockerfile \
  --load \
  /mnt/c/Users/Ming/Desktop/qData/qdata-service-quality-ce # Change the context path to your own path

# Build ARM64 (adapted to Kunpeng, Feiteng, Raspberry Pi and other ARM servers)
docker buildx build \
  --platform linux/arm64 \
  --no-cache \
  -t crpi-kf13onfj0v8f6jax.cn-shanghai.personal.cr.aliyuncs.com/qiantongkeji/qdata-service-quality-ce:1.6.9 \
  --file=docker/Dockerfile \
  --load \
  /mnt/c/Users/Ming/Desktop/qData/qdata-service-quality-ce # Change the context path to your own path

# Check if ARM64 is supported
docker inspect crpi-kf13onfj0v8f6jax.cn-shanghai.personal.cr.aliyuncs.com/qiantongkeji/qdata-service-quality-ce:1.6.9 --format '{{.Architecture}}'

# Delete the previously built builder (optional but recommended, keep it clean)
docker buildx rm qdata-builder

# Start new container
docker run -d \
  --name qdata-service-quality-ce \
  -p 8083:8083 \
  crpi-kf13onfj0v8f6jax.cn-shanghai.personal.cr.aliyuncs.com/qiantongkeji/qdata-service-quality-ce:1.6.9
