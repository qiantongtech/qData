import request from '@/utils/request'

// Query the data integration scheduling information list
export function listTaskScheduler(query) {
  return request({
    url: '/mc/taskScheduler/list',
    method: 'get',
    params: query
  })
}

// Query detailed data integration scheduling information
export function getTaskScheduler(id) {
  return request({
    url: '/mc/taskScheduler/' + id,
    method: 'get'
  })
}

// Added data integration scheduling information
export function addTaskScheduler(data) {
  return request({
    url: '/mc/taskScheduler',
    method: 'post',
    data: data
  })
}

// Modify data integration scheduling information
export function updateTaskScheduler(data) {
  return request({
    url: '/mc/taskScheduler',
    method: 'put',
    data: data
  })
}

// Delete data integration scheduling information
export function delTaskScheduler(id) {
  return request({
    url: '/mc/taskScheduler/' + id,
    method: 'delete'
  })
}
