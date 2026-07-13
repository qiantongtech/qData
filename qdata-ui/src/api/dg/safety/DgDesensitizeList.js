import request from '@/utils/request'

// Query desensitization list list
export function listDgDesensitizeList(query) {
  return request({
    url: '/dg/DgDesensitizeList/list',
    method: 'get',
    params: query
  })
}

// Query the desensitization list list associated with the desensitization rule (pass the ruleId more than the desensitization list list)
export function listDgDesensitizeListByRuleId(query) {
  return request({
    url: '/dg/DgDesensitizeList/listByRuleId',
    method: 'get',
    params: query
  })
}

// Check the desensitization list details
export function getDgDesensitizeList(id) {
  return request({
    url: '/dg/DgDesensitizeList/' + id,
    method: 'get'
  })
}

// Added desensitization list
export function addDgDesensitizeList(data) {
  return request({
    url: '/dg/DgDesensitizeList',
    method: 'post',
    data: data
  })
}

// Modify desensitization list
export function updateDgDesensitizeList(data) {
  return request({
    url: '/dg/DgDesensitizeList',
    method: 'put',
    data: data
  })
}

// Delete desensitization list
export function delDgDesensitizeList(ids) {
  const idStr = Array.isArray(ids) ? ids.join(',') : ids
  return request({
    url: '/dg/DgDesensitizeList/' + idStr,
    method: 'delete'
  })
}
