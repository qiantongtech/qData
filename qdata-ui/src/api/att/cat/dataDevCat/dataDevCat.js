import request from '@/utils/request'

// 查询数据开发类目管理列表
export function listAttDataDevCat(query) {
  return request({
    url: '/att/dataDevCat/list',
    method: 'get',
    params: query
  })
}

// 查询数据开发类目管理详细
export function getAttDataDevCat(id) {
  return request({
    url: '/att/dataDevCat/' + id,
    method: 'get'
  })
}

// 新增数据开发类目管理
export function addAttDataDevCat(data) {
  return request({
    url: '/att/dataDevCat',
    method: 'post',
    data: data
  })
}

// 修改数据开发类目管理
export function updateAttDataDevCat(data) {
  return request({
    url: '/att/dataDevCat',
    method: 'put',
    data: data
  })
}

// 删除数据开发类目管理
export function delAttDataDevCat(id) {
  return request({
    url: '/att/dataDevCat/' + id,
    method: 'delete'
  })
}
