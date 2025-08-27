import request from '@/utils/request'

// 查询标签与资产关联关系列表
export function listAttTagAssetRel(query) {
  return request({
    url: '/att/AttTagAssetRel/list',
    method: 'get',
    params: query
  })
}

// 查询标签与资产关联关系详细
export function getAttTagAssetRel(ID) {
  return request({
    url: '/att/AttTagAssetRel/' + ID,
    method: 'get'
  })
}

// 新增标签与资产关联关系
export function addAttTagAssetRel(data) {
  return request({
    url: '/att/AttTagAssetRel',
    method: 'post',
    data: data
  })
}

// 修改标签与资产关联关系
export function updateAttTagAssetRel(data) {
  return request({
    url: '/att/AttTagAssetRel',
    method: 'put',
    data: data
  })
}

// 删除标签与资产关联关系
export function delAttTagAssetRel(ID) {
  return request({
    url: '/att/AttTagAssetRel/' + ID,
    method: 'delete'
  })
}

export function delByTagIdAndAesstId(query) {
  return request({
    url: '/att/AttTagAssetRel/delByTagIdAndAesstId',
    method: 'delete',
    params: query
  })
}

