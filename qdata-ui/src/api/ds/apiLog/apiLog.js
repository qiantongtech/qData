import request from '@/utils/request'

// 查询API服务调用日志列表
export function listApiLog(query) {
  return request({
    url: '/ds/apiLog/list',
    method: 'get',
    params: query
  })
}

// 查询API服务调用日志详细
export function getApiLog(ID) {
  return request({
    url: '/ds/apiLog/' + ID,
    method: 'get'
  })
}

// 新增API服务调用日志
export function addApiLog(data) {
  return request({
    url: '/ds/apiLog',
    method: 'post',
    data: data
  })
}

// 修改API服务调用日志
export function updateApiLog(data) {
  return request({
    url: '/ds/apiLog',
    method: 'put',
    data: data
  })
}

// 删除API服务调用日志
export function delApiLog(ID) {
  return request({
    url: '/ds/apiLog/' + ID,
    method: 'delete'
  })
}
