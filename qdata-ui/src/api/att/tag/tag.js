import request from '@/utils/request'
// 查询标签管理列表
export function listAttTag(query) {
  return request({
    url: '/att/tag/list',
    method: 'get',
    params: query
  })
}
export function listDict(query) {
  return request({
    url: '/att/tag/listDict',
    method: 'get',
    params: query
  })
}

// 查询标签管理详细
export function getAttTag(id) {
  return request({
    url: '/att/tag/' + id,
    method: 'get'
  })
}

// 新增标签管理
export function addAttTag(data) {
  return request({
    url: '/att/tag',
    method: 'post',
    data: data
  })
}

// 修改标签管理
export function updateAttTag(data) {
  return request({
    url: '/att/tag',
    method: 'put',
    data: data
  })
}

// 删除标签管理
export function delAttTag(id) {
  return request({
    url: '/att/tag/' + id,
    method: 'delete'
  })
}
