import request from '@/utils/request'

// Query collection task instance list
export function listTaskInstance(query) {
  return request({
    url: '/mc/taskInstance/list',
    method: 'get',
    params: query
  })
}

// Query collection task instance details
export function getTaskInstance(id) {
  return request({
    url: '/mc/taskInstance/' + id,
    method: 'get'
  })
}

// Added collection task instance
export function addTaskInstance(data) {
  return request({
    url: '/mc/taskInstance',
    method: 'post',
    data: data
  })
}

// Modify collection task instance
export function updateTaskInstance(data) {
  return request({
    url: '/mc/taskInstance',
    method: 'put',
    data: data
  })
}

// Delete collection task instance
export function delTaskInstance(id) {
  return request({
    url: '/mc/taskInstance/' + id,
    method: 'delete'
  })
}
