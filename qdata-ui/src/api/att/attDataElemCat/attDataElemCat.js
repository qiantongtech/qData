import request from '@/utils/request'

// 查询数据元类目管理列表
export function listAttDataElemCat(query) {
  return request({
    url: '/att/attDataElemCat/list',
    method: 'get',
    params: query
  })
}

// 查询数据元类目管理详细
export function getAttDataElemCat(ID) {
  return request({
    url: '/att/attDataElemCat/' + ID,
    method: 'get'
  })
}

// 新增数据元类目管理
export function addAttDataElemCat(data) {
  return request({
    url: '/att/attDataElemCat',
    method: 'post',
    data: data
  })
}

// 修改数据元类目管理
export function updateAttDataElemCat(data) {
  return request({
    url: '/att/attDataElemCat',
    method: 'put',
    data: data
  })
}

// 删除数据元类目管理
export function delAttDataElemCat(ID) {
  return request({
    url: '/att/attDataElemCat/' + ID,
    method: 'delete'
  })
}
