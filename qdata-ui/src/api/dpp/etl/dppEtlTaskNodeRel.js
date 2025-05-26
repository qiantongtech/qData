import request from '@/utils/request'

// 查询数据集成任务节点关系列表
export function listDppEtlTaskNodeRel(query) {
  return request({
    url: '/dpp/dppEtlTaskNodeRel/list',
    method: 'get',
    params: query
  })
}

// 查询数据集成任务节点关系详细
export function getDppEtlTaskNodeRel(id) {
  return request({
    url: '/dpp/dppEtlTaskNodeRel/' + id,
    method: 'get'
  })
}

// 新增数据集成任务节点关系
export function addDppEtlTaskNodeRel(data) {
  return request({
    url: '/dpp/dppEtlTaskNodeRel',
    method: 'post',
    data: data
  })
}

// 修改数据集成任务节点关系
export function updateDppEtlTaskNodeRel(data) {
  return request({
    url: '/dpp/dppEtlTaskNodeRel',
    method: 'put',
    data: data
  })
}

// 删除数据集成任务节点关系
export function delDppEtlTaskNodeRel(id) {
  return request({
    url: '/dpp/dppEtlTaskNodeRel/' + id,
    method: 'delete'
  })
}
