import request from '@/utils/request'

// 查询服务资源门户区域字典列表
export function listRpDict(query) {
  return request({
    url: '/rp/rpDict/list',
    method: 'get',
    params: query
  })
}

// 查询服务资源门户区域字典详细
export function getRpDict(ID) {
  return request({
    url: '/rp/rpDict/' + ID,
    method: 'get'
  })
}

// 新增服务资源门户区域字典
export function addRpDict(data) {
  return request({
    url: '/rp/rpDict',
    method: 'post',
    data: data
  })
}

// 修改服务资源门户区域字典
export function updateRpDict(data) {
  return request({
    url: '/rp/rpDict',
    method: 'put',
    data: data
  })
}

// 删除服务资源门户区域字典
export function delRpDict(ID) {
  return request({
    url: '/rp/rpDict/' + ID,
    method: 'delete'
  })
}

// 查询服务资源门户区域字典列表
export function dictTreeSelect(query) {
  return request({
    url: '/rp/rpDict/deptTree',
    method: 'get',
    params: query
  })
}
