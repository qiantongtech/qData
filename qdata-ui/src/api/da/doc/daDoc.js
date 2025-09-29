import request from '@/utils/request'

// 查询文档数据管理列表
export function listDaDoc(query) {
  return request({
    url: '/da/daDoc/list',
    method: 'get',
    params: query
  })
}

// 查询文档数据管理详细
export function getDaDoc(id) {
  return request({
    url: '/da/daDoc/' + id,
    method: 'get'
  })
}

// 新增文档数据管理
export function addDaDoc(data) {
  return request({
    url: '/da/daDoc',
    method: 'post',
    data: data
  })
}

// 修改文档数据管理
export function updateDaDoc(data) {
  return request({
    url: '/da/daDoc',
    method: 'put',
    data: data
  })
}

// 删除文档数据管理
export function delDaDoc(id) {
  return request({
    url: '/da/daDoc/' + id,
    method: 'delete'
  })
}
