import request from '@/utils/request'

// Query the list of audit rules
export function listDgRule(query) {
  return request({
    url: '/dg/auditRule/list',
    method: 'get',
    params: query
  })
}

// Query detailed audit rules
export function getDgRule(id) {
  return request({
    url: '/dg/auditRule/' + id,
    method: 'get'
  })
}

// Add new audit rules
export function addDgRule(data) {
  return request({
    url: '/dg/auditRule',
    method: 'post',
    data: data
  })
}

// Modify audit rules
export function updateDgRule(data) {
  return request({
    url: '/dg/auditRule',
    method: 'put',
    data: data
  })
}

// Delete audit rules
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
