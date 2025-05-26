import request from '@/utils/request'

// 查询数据集成任务节点关系-日志列表
export function listDppEtlTaskNodeRelLog(query) {
  return request({
    url: '/dpp/dppEtlTaskNodeRelLog/list',
    method: 'get',
    params: query
  })
}

// 查询数据集成任务节点关系-日志详细
export function getDppEtlTaskNodeRelLog(id) {
  return request({
    url: '/dpp/dppEtlTaskNodeRelLog/' + id,
    method: 'get'
  })
}

// 新增数据集成任务节点关系-日志
export function addDppEtlTaskNodeRelLog(data) {
  return request({
    url: '/dpp/dppEtlTaskNodeRelLog',
    method: 'post',
    data: data
  })
}

// 修改数据集成任务节点关系-日志
export function updateDppEtlTaskNodeRelLog(data) {
  return request({
    url: '/dpp/dppEtlTaskNodeRelLog',
    method: 'put',
    data: data
  })
}

// 删除数据集成任务节点关系-日志
export function delDppEtlTaskNodeRelLog(id) {
  return request({
    url: '/dpp/dppEtlTaskNodeRelLog/' + id,
    method: 'delete'
  })
}
