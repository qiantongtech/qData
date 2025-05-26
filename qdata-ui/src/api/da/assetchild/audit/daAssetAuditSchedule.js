import request from '@/utils/request'

// 查询资产稽查调度列表
export function listDaAssetAuditSchedule(query) {
  return request({
    url: '/da/daAssetAuditSchedule/list',
    method: 'get',
    params: query
  })
}

// 查询资产稽查调度详细
export function getDaAssetAuditSchedule(id) {
  return request({
    url: '/da/daAssetAuditSchedule/' + id,
    method: 'get'
  })
}

// 新增资产稽查调度
export function addDaAssetAuditSchedule(data) {
  return request({
    url: '/da/daAssetAuditSchedule',
    method: 'post',
    data: data
  })
}

// 修改资产稽查调度
export function updateDaAssetAuditSchedule(data) {
  return request({
    url: '/da/daAssetAuditSchedule',
    method: 'put',
    data: data
  })
}

// 删除资产稽查调度
export function delDaAssetAuditSchedule(id) {
  return request({
    url: '/da/daAssetAuditSchedule/' + id,
    method: 'delete'
  })
}
