import request from '@/utils/request'

// 查询标准信息分类管理列表
export function listAttDocumentCat(query) {
  return request({
    url: '/att/documentCat/list',
    method: 'get',
    params: query
  })
}

// 查询标准信息分类管理详细
export function getAttDocumentCat(id) {
  return request({
    url: '/att/documentCat/' + id,
    method: 'get'
  })
}

// 新增标准信息分类管理
export function addAttDocumentCat(data) {
  return request({
    url: '/att/documentCat',
    method: 'post',
    data: data
  })
}

// 修改标准信息分类管理
export function updateAttDocumentCat(data) {
  return request({
    url: '/att/documentCat',
    method: 'put',
    data: data
  })
}

// 删除标准信息分类管理
export function delAttDocumentCat(id) {
  return request({
    url: '/att/documentCat/' + id,
    method: 'delete'
  })
}
