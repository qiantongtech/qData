import request from '@/utils/request'

// 查询系统配置列表
export function listContent(query) {
  return request({
    url: '/system/content/list',
    method: 'get',
    params: query
  })
}

// 查询系统配置详细
export function getContent(id) {
  return request({
    url: '/sys/content/' + id,
    method: 'get'
  })
}

// 新增系统配置
export function addContent(data) {
  return request({
    url: '/system/content',
    method: 'post',
    data: data
  })
}

// 修改系统配置
export function updateContent(data) {
  return request({
    url: '/system/content/edit',
    method: 'post',
    data: data
  })
}

// 删除系统配置
export function delContent(id) {
  return request({
    url: '/system/content/' + id,
    method: 'delete'
  })
}
