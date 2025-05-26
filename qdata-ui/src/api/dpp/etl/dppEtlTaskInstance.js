import request from '@/utils/request'

// 查询数据集成任务实例列表
export function listDppEtlTaskInstance(query) {
  return request({
    url: '/dpp/dppEtlTaskInstance/list',
    method: 'get',
    params: query
  })
}

// 查询数据集成任务实例详细
export function getDppEtlTaskInstance(id) {
  return request({
    url: '/dpp/dppEtlTaskInstance/' + id,
    method: 'get'
  })
}

// 新增数据集成任务实例
export function addDppEtlTaskInstance(data) {
  return request({
    url: '/dpp/dppEtlTaskInstance',
    method: 'post',
    data: data
  })
}

// 修改数据集成任务实例
export function updateDppEtlTaskInstance(data) {
  return request({
    url: '/dpp/dppEtlTaskInstance',
    method: 'put',
    data: data
  })
}

// 删除数据集成任务实例
export function delDppEtlTaskInstance(id) {
  return request({
    url: '/dpp/dppEtlTaskInstance/' + id,
    method: 'delete'
  })
}


// 查询数据集成任务实例列表
export function listDppEtlTreeList(query) {
  return request({
    url: '/dpp/dppEtlTaskInstance/treeList',
    method: 'get',
    params: query
  })
}
// 获取子任务列表
export function subNodeList(query) {
  return request({
    url: '/dpp/dppEtlTaskInstance/subNodeList',
    method: 'get',
    params: query
  })
}

