import request from '@/utils/request'

// 查询物化模型记录列表
export function listDpModelMaterialized(query) {
  return request({
    url: '/dp/dpModelMaterialized/list',
    method: 'get',
    params: query
  })
}

// 查询物化模型记录详细
export function getDpModelMaterialized(id) {
  return request({
    url: '/dp/dpModelMaterialized/' + id,
    method: 'get'
  })
}

// 新增物化模型记录
export function addDpModelMaterialized(data) {
  return request({
    url: '/dp/dpModelMaterialized',
    method: 'post',
    data: data
  })
}

// 修改物化模型记录
export function updateDpModelMaterialized(data) {
  return request({
    url: '/dp/dpModelMaterialized',
    method: 'put',
    data: data
  })
}

// 删除物化模型记录
export function delDpModelMaterialized(id) {
  return request({
    url: '/dp/dpModelMaterialized/' + id,
    method: 'delete'
  })
}
