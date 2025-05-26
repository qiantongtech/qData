import request from '@/utils/request'

// 查询数据集成调度信息列表
export function listDppEtlScheduler(query) {
  return request({
    url: '/dpp/dppEtlScheduler/list',
    method: 'get',
    params: query
  })
}

// 查询数据集成调度信息详细
export function getDppEtlScheduler(id) {
  return request({
    url: '/dpp/dppEtlScheduler/' + id,
    method: 'get'
  })
}

// 新增数据集成调度信息
export function addDppEtlScheduler(data) {
  return request({
    url: '/dpp/dppEtlScheduler',
    method: 'post',
    data: data
  })
}

// 修改数据集成调度信息
export function updateDppEtlScheduler(data) {
  return request({
    url: '/dpp/dppEtlScheduler',
    method: 'put',
    data: data
  })
}

// 删除数据集成调度信息
export function delDppEtlScheduler(id) {
  return request({
    url: '/dpp/dppEtlScheduler/' + id,
    method: 'delete'
  })
}
