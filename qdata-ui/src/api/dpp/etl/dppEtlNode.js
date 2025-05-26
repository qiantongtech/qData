import request from '@/utils/request'

// 查询数据集成节点列表
export function listDppEtlNode(query) {
  return request({
    url: '/dpp/dppEtlNode/list',
    method: 'get',
    params: query
  })
}

// 查询数据集成节点详细
export function getDppEtlNode(id) {
  return request({
    url: '/dpp/dppEtlNode/' + id,
    method: 'get'
  })
}

// 新增数据集成节点
export function addDppEtlNode(data) {
  return request({
    url: '/dpp/dppEtlNode',
    method: 'post',
    data: data
  })
}

// 修改数据集成节点
export function updateDppEtlNode(data) {
  return request({
    url: '/dpp/dppEtlNode',
    method: 'put',
    data: data
  })
}

// 删除数据集成节点
export function delDppEtlNode(id) {
  return request({
    url: '/dpp/dppEtlNode/' + id,
    method: 'delete'
  })
}
