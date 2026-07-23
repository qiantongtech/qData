

import request from '@/utils/request.js'
// Query tag management list
export function listAttTag(query) {
  return request({
    url: '/da/term/list',
    method: 'get',
    params: query
  })
}
export function listDict(query) {
  return request({
    url: '/da/term/listDict',
    method: 'get',
    params: query
  })
}

// Query tag management details
export function getAttTag(id) {
  return request({
    url: '/da/term/' + id,
    method: 'get'
  })
}

// Add tag management
export function addAttTag(data) {
  return request({
    url: '/da/term',
    method: 'post',
    data: data
  })
}

// Modify tag management
export function updateAttTag(data) {
  return request({
    url: '/da/term',
    method: 'put',
    data: data
  })
}

// Delete tag management
export function delAttTag(id) {
  return request({
    url: '/da/term/' + id,
    method: 'delete'
  })
}
