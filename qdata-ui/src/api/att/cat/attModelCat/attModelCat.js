import request from '@/utils/request.js'

// 查询逻辑模型类目管理列表
export function listAttModelCat(query) {
  return request({
    url: '/att/attModelCat/list',
    method: 'get',
    params: query
  })
}

// 查询逻辑模型类目管理详细
export function getAttModelCat(ID) {
  return request({
    url: '/att/attModelCat/' + ID,
    method: 'get'
  })
}

// 新增逻辑模型类目管理
export function addAttModelCat(data) {
  return request({
    url: '/att/attModelCat',
    method: 'post',
    data: data
  })
}

// 修改逻辑模型类目管理
export function updateAttModelCat(data) {
  return request({
    url: '/att/attModelCat',
    method: 'put',
    data: data
  })
}

// 删除逻辑模型类目管理
export function delAttModelCat(ID) {
  return request({
    url: '/att/attModelCat/' + ID,
    method: 'delete'
  })
}
