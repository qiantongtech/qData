import request from '@/utils/request'

// 查询数据资产字段列表
export function listDaAssetColumn(query) {
  return request({
    url: '/da/daAssetColumn/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产字段详细
export function getDaAssetColumn(id) {
  return request({
    url: '/da/daAssetColumn/' + id,
    method: 'get'
  })
}

// 新增数据资产字段
export function addDaAssetColumn(data) {
  return request({
    url: '/da/daAssetColumn',
    method: 'post',
    data: data
  })
}

// 修改数据资产字段
export function updateDaAssetColumn(data) {
  return request({
    url: '/da/daAssetColumn',
    method: 'put',
    data: data
  })
}

// 删除数据资产字段
export function delDaAssetColumn(id) {
  return request({
    url: '/da/daAssetColumn/' + id,
    method: 'delete'
  })
}
// 預覽

export function preview(data) {
  return request({
    url: '/da/daAsset/preview',
    method: 'post',
    data: data
  })
}