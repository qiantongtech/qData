import request from '@/utils/request'

// 查询应用API服务关联列表
export function listClientApiRel(query) {
  return request({
    url: '/att/clientApiRel/list',
    method: 'get',
    params: query
  })
}

// 查询应用API服务关联详细
export function getClientApiRel(id) {
  return request({
    url: '/att/clientApiRel/' + id,
    method: 'get'
  })
}

// 新增应用API服务关联
export function addClientApiRel(data) {
  return request({
    url: '/att/clientApiRel',
    method: 'post',
    data: data
  })
}

// 修改应用API服务关联
export function updateClientApiRel(data) {
  return request({
    url: '/att/clientApiRel',
    method: 'put',
    data: data
  })
}

// 删除应用API服务关联
export function delClientApiRel(id) {
  return request({
    url: '/att/clientApiRel/' + id,
    method: 'delete'
  })
}
