import request from '@/utils/request'

// 查询采集任务列表
export function listTask(query) {
  return request({
    url: '/mc/task/list',
    method: 'get',
    params: query
  })
}

// 查询采集任务详细
export function getTask(id) {
  return request({
    url: '/mc/task/' + id,
    method: 'get'
  })
}

// 新增采集任务
export function addTask(data) {
  return request({
    url: '/mc/task',
    method: 'post',
    data: data
  })
}

// 修改采集任务
export function updateTask(data) {
  return request({
    url: '/mc/task',
    method: 'put',
    data: data
  })
}

// 删除采集任务
export function delTask(id) {
  return request({
    url: '/mc/task/' + id,
    method: 'delete'
  })
}

// 获取采集范围
export function getRealtimeMcTaskScopeList(id) {
  return request({
    url: '/mc/task/getRealtimeMcTaskScopeList/' + id,
    method: 'get'
  })
}

// 任务上下线
export function updateReleaseJobTask(data) {
  return request({
    url: 'mc/task/updateReleaseJobTask',
    method: 'post',
    data
  })
}

// 调度上下线
export function updateReleaseSchedule(data) {
  return request({
    url: 'mc/task/updateReleaseSchedule',
    method: 'post',
    data
  })
}

//执行一次
export function runJobOnce(data) {
  return request({
    url: `/mc/task/runJobOnce`,
    method: 'post',
    data
  })
}

// 获取来源系统树
export function sourceSystemTree(query) {
  return request({
    url: '/mc/task/sourceSystemTree',
    method: 'get',
    params: query
  })
}