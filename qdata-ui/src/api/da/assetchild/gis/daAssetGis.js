import request from '@/utils/request'

// 查询数据资产-地理空间服务列表
export function listDaAssetGis(query) {
  return request({
    url: '/da/daAssetGis/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产-地理空间服务详细
export function getDaAssetGis(id) {
  return request({
    url: '/da/daAssetGis/' + id,
    method: 'get'
  })
}

// 新增数据资产-地理空间服务
export function addDaAssetGis(data) {
  return request({
    url: '/da/daAssetGis',
    method: 'post',
    data: data
  })
}

// 修改数据资产-地理空间服务
export function updateDaAssetGis(data) {
  return request({
    url: '/da/daAssetGis',
    method: 'put',
    data: data
  })
}

// 删除数据资产-地理空间服务
export function delDaAssetGis(id) {
  return request({
    url: '/da/daAssetGis/' + id,
    method: 'delete'
  })
}
