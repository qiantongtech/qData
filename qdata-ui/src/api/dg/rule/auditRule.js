import request from '@/utils/request'

// 查询稽查规则列表
export function listDgRule(query) {
  return request({
    url: '/dg/auditRule/list',
    method: 'get',
    params: query
  })
}

// 查询稽查规则详细
export function getDgRule(id) {
  return request({
    url: '/dg/auditRule/' + id,
    method: 'get'
  })
}

// 新增稽查规则
export function addDgRule(data) {
  return request({
    url: '/dg/auditRule',
    method: 'post',
    data: data
  })
}

// 修改稽查规则
export function updateDgRule(data) {
  return request({
    url: '/dg/auditRule',
    method: 'put',
    data: data
  })
}

// 删除稽查规则
export function delDgRule(id) {
  return request({
    url: '/dg/auditRule/' + id,
    method: 'delete'
  })
}

// tree
export function treeDgRule(params) {
  return request({
    url: '/dg/auditRule/tree',
    method: 'get',
    params
  })
}
