import request from '@/utils/request.js'

// 查询证书管理列表
export function listCert(query) {
  return request({
    url: '/ca/cert/list',
    method: 'get',
    params: query
  })
}

// 查询证书管理详细
export function getCert(id) {
  return request({
    url: '/ca/cert/' + id,
    method: 'get'
  })
}

// 新增证书管理
export function addCert(data) {
  return request({
    url: '/ca/cert',
    method: 'post',
    data: data
  })
}

// 修改证书管理
export function updateCert(data) {
  return request({
    url: '/ca/cert',
    method: 'put',
    data: data
  })
}

// 删除证书管理
export function delCert(id) {
  return request({
    url: '/ca/cert/' + id,
    method: 'delete'
  })
}
