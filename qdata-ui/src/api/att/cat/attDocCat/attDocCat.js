import request from '@/utils/request.js'

// 查询数据资产文档类目管理列表
export function listAttDocCat(query) {
  return request({
    url: '/att/AttDocCat/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产文档类目管理详细
export function getAttDocCat(id) {
  return request({
    url: '/att/AttDocCat/' + id,
    method: 'get'
  })
}

// 新增数据资产文档类目管理
export function addAttDocCat(data) {
  return request({
    url: '/att/AttDocCat',
    method: 'post',
    data: data
  })
}

// 修改数据资产文档类目管理
export function updateAttDocCat(data) {
  return request({
    url: '/att/AttDocCat',
    method: 'put',
    data: data
  })
}

// 删除数据资产文档类目管理
export function delAttDocCat(id) {
  return request({
    url: '/att/AttDocCat/' + id,
    method: 'delete'
  })
}
