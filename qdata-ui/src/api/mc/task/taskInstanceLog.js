import request from '@/utils/request'

// 查询采集任务实例-日志列表
export function listTaskInstanceLog(query) {
  return request({
    url: '/mc/taskInstanceLog/list',
    method: 'get',
    params: query
  })
}

// 查询采集任务实例-日志详细
export function getTaskInstanceLog(taskInstanceId) {
  return request({
    url: '/mc/taskInstanceLog/' + taskInstanceId,
    method: 'get'
  })
}

// 新增采集任务实例-日志
export function addTaskInstanceLog(data) {
  return request({
    url: '/mc/taskInstanceLog',
    method: 'post',
    data: data
  })
}

// 修改采集任务实例-日志
export function updateTaskInstanceLog(data) {
  return request({
    url: '/mc/taskInstanceLog',
    method: 'put',
    data: data
  })
}

// 删除采集任务实例-日志
export function delTaskInstanceLog(taskInstanceId) {
  return request({
    url: '/mc/taskInstanceLog/' + taskInstanceId,
    method: 'delete'
  })
}
