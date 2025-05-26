import request from '@/utils/request'

// 查询用户类型列表
export function listUserType(query) {
  return request({
    url: '/example/userType/list',
    method: 'get',
    params: query
  })
}

// 查询用户类型详细
export function getUserType(id) {
  return request({
    url: '/example/userType/' + id,
    method: 'get'
  })
}

// 新增用户类型
export function addUserType(data) {
  return request({
    url: '/example/userType',
    method: 'post',
    data: data
  })
}

// 修改用户类型
export function updateUserType(data) {
  return request({
    url: '/example/userType',
    method: 'put',
    data: data
  })
}

// 删除用户类型
export function delUserType(id) {
  return request({
    url: '/example/userType/' + id,
    method: 'delete'
  })
}
