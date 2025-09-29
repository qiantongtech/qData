import request from '@/utils/request'

// 查询数据发现库信息列表
export function listDaDiscoveryTable(query) {
  return request({
    url: '/da/discoveryTable/list',
    method: 'get',
    params: query
  })
}

// 查询数据发现库信息列表
export function getDaDiscoveryTableList(query) {
  return request({
    url: '/da/discoveryTable/getDaDiscoveryTableList',
    method: 'get',
    params: query
  })
}

// 查询数据发现库信息详细
export function getDaDiscoveryTable(id) {
  return request({
    url: '/da/discoveryTable/' + id,
    method: 'get'
  })
}

// 新增数据发现库信息
export function addDaDiscoveryTable(data) {
  return request({
    url: '/da/discoveryTable',
    method: 'post',
    data: data
  })
}
// 新增数据发现库信息
export function commitOrRevokeDiscoveryInfo(data) {
  return request({
    url: '/da/discoveryTable/commitOrRevokeDiscoveryInfo',
    method: 'post',
    data: data
  })
}

// 修改数据发现库信息
export function updateDaDiscoveryTable(data) {
  return request({
    url: '/da/discoveryTable',
    method: 'put',
    data: data
  })
}

// 删除数据发现库信息
export function delDaDiscoveryTable(id) {
  return request({
    url: '/da/discoveryTable/' + id,
    method: 'delete'
  })
}
// 表字段
export function getDaDiscoveryColumnList(params) {
  return request({
    url: '/da/discoveryColumn/getDaDiscoveryColumnList',
    method: 'get',
    params: params
  })
}

export function preview(data) {
  return request({
    url: '/da/discoveryTable/preview',
    method: 'post',
    data: data
  })
}
