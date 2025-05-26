import request from '@/utils/request'

// 查询数据发现任务日志列表
export function listDaDiscoveryTaskLog(query) {
  return request({
    url: '/da/daDiscoveryTaskLog/list',
    method: 'get',
    params: query
  })
}
// 查看
export function logDetailCat(query) {
  return request({
    url: '/da/daDiscoveryTaskLog/logDetailCat',
    method: 'get',
    params: query
  })
}

export function downloadLog(query) {
  return request({
    url: '/da/daDiscoveryTaskLog/downloadLog',
    method: 'get',
    params: query
  })
}


// 查询数据发现任务日志详细
export function getDaDiscoveryTaskLog(id) {
  return request({
    url: '/da/daDiscoveryTaskLog/' + id,
    method: 'get'
  })
}

// 新增数据发现任务日志
export function addDaDiscoveryTaskLog(data) {
  return request({
    url: '/da/daDiscoveryTaskLog',
    method: 'post',
    data: data
  })
}

// 修改数据发现任务日志
export function updateDaDiscoveryTaskLog(data) {
  return request({
    url: '/da/daDiscoveryTaskLog',
    method: 'put',
    data: data
  })
}

// 删除数据发现任务日志
export function delDaDiscoveryTaskLog(id) {
  return request({
    url: '/da/daDiscoveryTaskLog/' + id,
    method: 'delete'
  })
}
