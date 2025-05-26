import request from '@/utils/request'

// 查询逻辑模型属性信息列表
export function listDpModelColumn(query) {
  return request({
    url: '/dp/dpModelColumn/list',
    method: 'get',
    params: query
  })
}

// 查询逻辑模型属性信息详细
export function getDpModelColumn(id) {
  return request({
    url: '/dp/dpModelColumn/' + id,
    method: 'get'
  })
}

// 新增逻辑模型属性信息
export function addDpModelColumn(data) {
  return request({
    url: '/dp/dpModelColumn',
    method: 'post',
    data: data
  })
}

// 修改逻辑模型属性信息
export function updateDpModelColumn(data) {
  return request({
    url: '/dp/dpModelColumn',
    method: 'put',
    data: data
  })
}

// 删除逻辑模型属性信息
export function delDpModelColumn(id) {
  return request({
    url: '/dp/dpModelColumn/' + id,
    method: 'delete'
  })
}
