

import request from '@/utils/request.js'

// 查询标签与术语关联关系列表
export function listAttTagAssetRel(query) {
  return request({
    url: '/att/tagAssetRel/list',
    method: 'get',
    params: query
  })
}

// 查询标签与术语关联关系详细
export function getAttTagAssetRel(ID) {
  return request({
    url: '/att/tagAssetRel/' + ID,
    method: 'get'
  })
}

// 新增标签与术语关联关系
export function addAttTagAssetRel(data) {
  return request({
    url: '/att/tagAssetRel',
    method: 'post',
    data: data
  })
}

// 修改标签与术语关联关系
export function updateAttTagAssetRel(data) {
  return request({
    url: '/att/tagAssetRel',
    method: 'put',
    data: data
  })
}

// 删除标签与术语关联关系
export function delAttTagAssetRel(ID) {
  return request({
    url: '/att/tagAssetRel/' + ID,
    method: 'delete'
  })
}

export function delByTagIdAndAesstId(query) {
  return request({
    url: '/da/termColumnRel/delByTagIdAndAesstId',
    method: 'delete',
    params: query
  })
}

