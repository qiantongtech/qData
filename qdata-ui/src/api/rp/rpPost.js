import request from '@/utils/request'

// 查询服务资源门户岗位列表
export function listRpPost(query) {
  return request({
    url: '/rp/rpPost/list',
    method: 'get',
    params: query
  })
}

// 查询服务资源门户岗位详细
export function getRpPost(postId) {
  return request({
    url: '/rp/rpPost/' + postId,
    method: 'get'
  })
}

// 新增服务资源门户岗位
export function addRpPost(data) {
  return request({
    url: '/rp/rpPost',
    method: 'post',
    data: data
  })
}

// 修改服务资源门户岗位
export function updateRpPost(data) {
  return request({
    url: '/rp/rpPost',
    method: 'put',
    data: data
  })
}

// 删除服务资源门户岗位
export function delRpPost(postId) {
  return request({
    url: '/rp/rpPost/' + postId,
    method: 'delete'
  })
}
