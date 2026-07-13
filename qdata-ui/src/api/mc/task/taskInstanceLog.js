import request from '@/utils/request'

// Query collection task instance-log list
export function listTaskInstanceLog(query) {
  return request({
    url: '/mc/taskInstanceLog/list',
    method: 'get',
    params: query
  })
}

// Query collection task instance-log details
export function getTaskInstanceLog(taskInstanceId) {
  return request({
    url: '/mc/taskInstanceLog/' + taskInstanceId,
    method: 'get'
  })
}

// Added new collection task instance-log
export function addTaskInstanceLog(data) {
  return request({
    url: '/mc/taskInstanceLog',
    method: 'post',
    data: data
  })
}

// Modify the collection task instance-log
export function updateTaskInstanceLog(data) {
  return request({
    url: '/mc/taskInstanceLog',
    method: 'put',
    data: data
  })
}

// Delete collection task instance-log
export function delTaskInstanceLog(taskInstanceId) {
  return request({
    url: '/mc/taskInstanceLog/' + taskInstanceId,
    method: 'delete'
  })
}
