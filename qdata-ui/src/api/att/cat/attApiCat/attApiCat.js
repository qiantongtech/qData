import request from '@/utils/request'

// 查询数据服务类目管理列表
export function listAttApiCat(query) {
  return request({
    url: '/att/AttApiCat/list',
    method: 'get',
    params: query
  })
}

// 查询数据服务类目管理详细
export function getAttApiCat(id) {
  return request({
    url: '/att/AttApiCat/' + id,
    method: 'get'
  })
}

// 新增数据服务类目管理
export function addAttApiCat(data) {
  return request({
    url: '/att/AttApiCat',
    method: 'post',
    data: data
  })
}

// 修改数据服务类目管理
export function updateAttApiCat(data) {
  return request({
    url: '/att/AttApiCat',
    method: 'put',
    data: data
  })
}

// 删除数据服务类目管理
export function delAttApiCat(id) {
  return request({
    url: '/att/AttApiCat/' + id,
    method: 'delete'
  })
}
