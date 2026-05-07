import request from '@/utils/request'

// 查询采集任务实例列表
export function listTaskInstance(query) {
  return request({
    url: '/mc/taskInstance/list',
    method: 'get',
    params: query
  })
}

// 查询采集任务实例详细
export function getTaskInstance(id) {
  return request({
    url: '/mc/taskInstance/' + id,
    method: 'get'
  })
}

// 新增采集任务实例
export function addTaskInstance(data) {
  return request({
    url: '/mc/taskInstance',
    method: 'post',
    data: data
  })
}

// 修改采集任务实例
export function updateTaskInstance(data) {
  return request({
    url: '/mc/taskInstance',
    method: 'put',
    data: data
  })
}

// 删除采集任务实例
export function delTaskInstance(id) {
  return request({
    url: '/mc/taskInstance/' + id,
    method: 'delete'
  })
}
