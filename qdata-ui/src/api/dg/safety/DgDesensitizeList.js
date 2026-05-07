import request from '@/utils/request'

// 查询脱敏清单列表
export function listDgDesensitizeList(query) {
  return request({
    url: '/dg/DgDesensitizeList/list',
    method: 'get',
    params: query
  })
}

// 查询脱敏规则关联脱敏清单列表（比脱敏清单列表多传 ruleId）
export function listDgDesensitizeListByRuleId(query) {
  return request({
    url: '/dg/DgDesensitizeList/listByRuleId',
    method: 'get',
    params: query
  })
}

// 查询脱敏清单详细
export function getDgDesensitizeList(id) {
  return request({
    url: '/dg/DgDesensitizeList/' + id,
    method: 'get'
  })
}

// 新增脱敏清单
export function addDgDesensitizeList(data) {
  return request({
    url: '/dg/DgDesensitizeList',
    method: 'post',
    data: data
  })
}

// 修改脱敏清单
export function updateDgDesensitizeList(data) {
  return request({
    url: '/dg/DgDesensitizeList',
    method: 'put',
    data: data
  })
}

// 删除脱敏清单
export function delDgDesensitizeList(ids) {
  const idStr = Array.isArray(ids) ? ids.join(',') : ids
  return request({
    url: '/dg/DgDesensitizeList/' + idStr,
    method: 'delete'
  })
}
