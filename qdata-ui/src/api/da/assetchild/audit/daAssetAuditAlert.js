import request from '@/utils/request'

// 查询数据资产-质量预警列表
export function listDaAssetAuditAlert(query) {
  return request({
    url: '/da/daAssetAuditAlert/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产-质量预警详细
export function getDaAssetAuditAlert(id) {
  return request({
    url: '/da/daAssetAuditAlert/' + id,
    method: 'get'
  })
}

// 新增数据资产-质量预警
export function addDaAssetAuditAlert(data) {
  return request({
    url: '/da/daAssetAuditAlert',
    method: 'post',
    data: data
  })
}

// 修改数据资产-质量预警
export function updateDaAssetAuditAlert(data) {
  return request({
    url: '/da/daAssetAuditAlert',
    method: 'put',
    data: data
  })
}

// 删除数据资产-质量预警
export function delDaAssetAuditAlert(id) {
  return request({
    url: '/da/daAssetAuditAlert/' + id,
    method: 'delete'
  })
}
