import request from '@/utils/request'

// 查询数据发现字段列表
export function listDaDiscoveryColumn(query) {
  return request({
    url: '/da/daDiscoveryColumn/list',
    method: 'get',
    params: query
  })
}

// 查询数据发现字段详细
export function getDaDiscoveryColumn(id) {
  return request({
    url: '/da/daDiscoveryColumn/' + id,
    method: 'get'
  })
}

// 新增数据发现字段
export function addDaDiscoveryColumn(data) {
  return request({
    url: '/da/daDiscoveryColumn',
    method: 'post',
    data: data
  })
}

// 修改数据发现字段
export function updateDaDiscoveryColumn(data) {
  return request({
    url: '/da/daDiscoveryColumn',
    method: 'put',
    data: data
  })
}

// 删除数据发现字段
export function delDaDiscoveryColumn(id) {
  return request({
    url: '/da/daDiscoveryColumn/' + id,
    method: 'delete'
  })
}
