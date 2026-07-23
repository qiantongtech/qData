import request from '@/utils/request'

// Query collection range list
export function listTaskScope(query) {
  return request({
    url: '/mc/taskScope/list',
    method: 'get',
    params: query
  })
}

// Query the collection range details
export function getTaskScope(id) {
  return request({
    url: '/mc/taskScope/' + id,
    method: 'get'
  })
}

// Add new collection range
export function addTaskScope(data) {
  return request({
    url: '/mc/taskScope',
    method: 'post',
    data: data
  })
}

// Modify collection range
export function updateTaskScope(data) {
  return request({
    url: '/mc/taskScope',
    method: 'put',
    data: data
  })
}

// Delete collection range
export function delTaskScope(id) {
  return request({
    url: '/mc/taskScope/' + id,
    method: 'delete'
  })
}
