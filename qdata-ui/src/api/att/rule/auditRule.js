import request from '@/utils/request'

// 查询稽查规则列表
export function listAttAuditRule(query) {
  return request({
    url: '/att/auditRule/list',
    method: 'get',
    params: query
  })
}

// 查询稽查规则详细
export function getAttAuditRule(id) {
  return request({
    url: '/att/auditRule/' + id,
    method: 'get'
  })
}

// 新增稽查规则
export function addAttAuditRule(data) {
  return request({
    url: '/att/auditRule',
    method: 'post',
    data: data
  })
}

// 修改稽查规则
export function updateAttAuditRule(data) {
  return request({
    url: '/att/auditRule',
    method: 'put',
    data: data
  })
}

// 删除稽查规则
export function delAttAuditRule(id) {
  return request({
    url: '/att/auditRule/' + id,
    method: 'delete'
  })
}

// tree
export function treeAttAuditRule(params) {
  return request({
    url: '/att/attAuditRule/tree',
    method: 'get',
    params
  })
}
