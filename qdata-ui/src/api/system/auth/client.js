import request from '@/utils/request.js'

// 查询应用管理列表
export function listClient(query) {
  return request({
    url: '/auth/client/list',
    method: 'get',
    params: query
  })
}

// 查询应用管理详细
export function getClient(id) {
  return request({
    url: '/auth/client/' + id,
    method: 'get'
  })
}

// 新增应用管理
export function addClient(data) {
  return request({
    url: '/auth/client',
    method: 'post',
    data: data
  })
}

// 修改应用管理
export function updateClient(data) {
  return request({
    url: '/auth/client',
    method: 'put',
    data: data
  })
}

// 删除应用管理
export function delClient(id) {
  return request({
    url: '/auth/client/' + id,
    method: 'delete'
  })
}
