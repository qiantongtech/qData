import request from '@/utils/request'

// 查询数据集成节点-日志列表
export function listDppEtlNodeLog(query) {
  return request({
    url: '/dpp/dppEtlNodeLog/list',
    method: 'get',
    params: query
  })
}

// 查询数据集成节点-日志详细
export function getDppEtlNodeLog(id) {
  return request({
    url: '/dpp/dppEtlNodeLog/' + id,
    method: 'get'
  })
}

// 新增数据集成节点-日志
export function addDppEtlNodeLog(data) {
  return request({
    url: '/dpp/dppEtlNodeLog',
    method: 'post',
    data: data
  })
}

// 修改数据集成节点-日志
export function updateDppEtlNodeLog(data) {
  return request({
    url: '/dpp/dppEtlNodeLog',
    method: 'put',
    data: data
  })
}

// 删除数据集成节点-日志
export function delDppEtlNodeLog(id) {
  return request({
    url: '/dpp/dppEtlNodeLog/' + id,
    method: 'delete'
  })
}
