/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

import request from '@/utils/request'

// 查询容器管理-服务器配置列表
export function listcontainerServer(query) {
  return request({
    url: '/system/containerServer/list',
    method: 'get',
    params: query
  })
}

// 查询容器管理-服务器配置详细
export function getcontainerServer(ID) {
  return request({
    url: '/system/containerServer/info',
    method: 'get',
    params: { id: ID }
  }).then(response=>{
      response.data.containerList?.forEach(i=>{
          if(i.CreatedAt) {
              i.CreatedAt=new Date(i.CreatedAt.replace(' CST', ''))
          }
      })
      return response;
  })
}


// 新增容器管理-服务器配置
export function addcontainerServer(data) {
  return request({
    url: '/system/containerServer',
    method: 'post',
    data: data
  })
}

// 修改容器管理-服务器配置
export function updatecontainerServer(data) {
  return request({
    url: '/system/containerServer',
    method: 'put',
    data: data
  })
}

// 删除容器管理-服务器配置
export function delcontainerServer(ID) {
  return request({
    url: '/system/containerServer/' + ID,
    method: 'delete'
  })
}
