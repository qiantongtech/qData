import request from '@/utils/request'

// 查询数据资产质量结果记录列表
export function listDaAssetAuditRule(query) {
  return request({
    url: '/da/daAssetAuditRule/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产质量结果记录详细
export function getDaAssetAuditRule(id) {
  return request({
    url: '/da/daAssetAuditRule/' + id,
    method: 'get'
  })
}

// 新增数据资产质量结果记录
export function addDaAssetAuditRule(data) {
  return request({
    url: '/da/daAssetAuditRule',
    method: 'post',
    data: data
  })
}

// 修改数据资产质量结果记录
export function updateDaAssetAuditRule(data) {
  return request({
    url: '/da/daAssetAuditRule',
    method: 'put',
    data: data
  })
}

// 删除数据资产质量结果记录
export function delDaAssetAuditRule(id) {
  return request({
    url: '/da/daAssetAuditRule/' + id,
    method: 'delete'
  })
}
