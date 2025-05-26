import request from '@/utils/request.js'

// 查询主体管理列表
export function listSubject(query) {
  return request({
    url: '/ca/subject/list',
    method: 'get',
    params: query
  })
}

// 查询主体管理详细
export function getSubject(id) {
  return request({
    url: '/ca/subject/' + id,
    method: 'get'
  })
}

// 新增主体管理
export function addSubject(data) {
  return request({
    url: '/ca/subject',
    method: 'post',
    data: data
  })
}

// 修改主体管理
export function updateSubject(data) {
  return request({
    url: '/ca/subject',
    method: 'put',
    data: data
  })
}

// 删除主体管理
export function delSubject(id) {
  return request({
    url: '/ca/subject/' + id,
    method: 'delete'
  })
}
