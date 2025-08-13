import request from '@/utils/request'

// 查询数据质量类目列表
export function listAttQualityCat(query) {
  return request({
    url: '/att/AttQualityCat/list',
    method: 'get',
    params: query
  })
}

// 查询数据质量类目详细
export function getAttQualityCat(id) {
  return request({
    url: '/att/AttQualityCat/' + id,
    method: 'get'
  })
}

// 新增数据质量类目
export function addAttQualityCat(data) {
  return request({
    url: '/att/AttQualityCat',
    method: 'post',
    data: data
  })
}

// 修改数据质量类目
export function updateAttQualityCat(data) {
  return request({
    url: '/att/AttQualityCat',
    method: 'put',
    data: data
  })
}

// 删除数据质量类目
export function delAttQualityCat(id) {
  return request({
    url: '/att/AttQualityCat/' + id,
    method: 'delete'
  })
}
