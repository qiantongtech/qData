import request from '@/utils/request.js'

// 查询应用和用户关联列表
export function listUser(query) {
  return request({
    url: '/auth/user/list',
    method: 'get',
    params: query
  })
}

// 查询应用和用户关联详细
export function getUser(clientId) {
  return request({
    url: '/auth/user/' + clientId,
    method: 'get'
  })
}

// 新增应用和用户关联
export function addUser(data) {
  return request({
    url: '/auth/user',
    method: 'post',
    data: data
  })
}

// 修改应用和用户关联
export function updateUser(data) {
  return request({
    url: '/auth/user',
    method: 'put',
    data: data
  })
}

// 删除应用和用户关联
export function delUser(clientId) {
  return request({
    url: '/auth/user/' + clientId,
    method: 'delete'
  })
}
