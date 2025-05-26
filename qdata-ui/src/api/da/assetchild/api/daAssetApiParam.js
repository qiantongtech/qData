import request from '@/utils/request'

// 查询数据资产-外部API-参数列表
export function listDaAssetApiParam(query) {
  return request({
    url: '/da/daAssetApiParam/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产-外部API-参数详细
export function getDaAssetApiParam(id) {
  return request({
    url: '/da/daAssetApiParam/' + id,
    method: 'get'
  })
}

// 新增数据资产-外部API-参数
export function addDaAssetApiParam(data) {
  return request({
    url: '/da/daAssetApiParam',
    method: 'post',
    data: data
  })
}

// 修改数据资产-外部API-参数
export function updateDaAssetApiParam(data) {
  return request({
    url: '/da/daAssetApiParam',
    method: 'put',
    data: data
  })
}

// 删除数据资产-外部API-参数
export function delDaAssetApiParam(id) {
  return request({
    url: '/da/daAssetApiParam/' + id,
    method: 'delete'
  })
}
