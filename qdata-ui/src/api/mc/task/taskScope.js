import request from '@/utils/request'

// 查询采集范围列表
export function listTaskScope(query) {
  return request({
    url: '/mc/taskScope/list',
    method: 'get',
    params: query
  })
}

// 查询采集范围详细
export function getTaskScope(id) {
  return request({
    url: '/mc/taskScope/' + id,
    method: 'get'
  })
}

// 新增采集范围
export function addTaskScope(data) {
  return request({
    url: '/mc/taskScope',
    method: 'post',
    data: data
  })
}

// 修改采集范围
export function updateTaskScope(data) {
  return request({
    url: '/mc/taskScope',
    method: 'put',
    data: data
  })
}

// 删除采集范围
export function delTaskScope(id) {
  return request({
    url: '/mc/taskScope/' + id,
    method: 'delete'
  })
}
