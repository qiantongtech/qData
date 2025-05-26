import request from '@/utils/request'

// 查询数据资产-外部API列表
export function listApi(query) {
  return request({
    url: '/da/api/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产-外部API详细
export function getApi(id) {
  return request({
    url: '/da/api/' + id,
    method: 'get'
  })
}

// 新增数据资产-外部API
export function addApi(data) {
  return request({
    url: '/da/api',
    method: 'post',
    data: data
  })
}

// 修改数据资产-外部API
export function updateApi(data) {
  return request({
    url: '/da/api',
    method: 'put',
    data: data
  })
}

// 删除数据资产-外部API
export function delApi(id) {
  return request({
    url: '/da/api/' + id,
    method: 'delete'
  })
}
