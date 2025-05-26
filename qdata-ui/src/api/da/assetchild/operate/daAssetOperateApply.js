import request from '@/utils/request'

// 查询数据资产操作申请列表
export function listDaAssetOperateApply(query) {
  return request({
    url: '/da/daAssetOperateApply/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产操作申请详细
export function getDaAssetOperateApply(id) {
  return request({
    url: '/da/daAssetOperateApply/' + id,
    method: 'get'
  })
}

// 新增数据资产操作申请
export function addDaAssetOperateApply(data) {
  return request({
    url: '/da/daAssetOperateApply',
    method: 'post',
    data: data
  })
}

// 修改数据资产操作申请
export function updateDaAssetOperateApply(data) {
  return request({
    url: '/da/daAssetOperateApply',
    method: 'put',
    data: data
  })
}

// 删除数据资产操作申请
export function delDaAssetOperateApply(id) {
  return request({
    url: '/da/daAssetOperateApply/' + id,
    method: 'delete'
  })
}
