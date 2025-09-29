import request from '@/utils/request'

// 查询服务资源门户用户与岗位关联列表
export function listRpUserPost(query) {
  return request({
    url: '/rp/rpUserPost/list',
    method: 'get',
    params: query
  })
}

// 查询服务资源门户用户与岗位关联详细
export function getRpUserPost(userId) {
  return request({
    url: '/rp/rpUserPost/' + userId,
    method: 'get'
  })
}

// 新增服务资源门户用户与岗位关联
export function addRpUserPost(data) {
  return request({
    url: '/rp/rpUserPost',
    method: 'post',
    data: data
  })
}

// 修改服务资源门户用户与岗位关联
export function updateRpUserPost(data) {
  return request({
    url: '/rp/rpUserPost',
    method: 'put',
    data: data
  })
}

// 删除服务资源门户用户与岗位关联
export function delRpUserPost(userId) {
  return request({
    url: '/rp/rpUserPost/' + userId,
    method: 'delete'
  })
}
