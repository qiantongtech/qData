import request from '@/utils/request'

// 查询数据资产操作记录列表
export function listDaAssetOperateLog(query) {
  return request({
    url: '/da/daAssetOperateLog/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产操作记录详细
export function getDaAssetOperateLog(id) {
  return request({
    url: '/da/daAssetOperateLog/' + id,
    method: 'get'
  })
}

// 新增数据资产操作记录
export function addDaAssetOperateLog(data) {
  return request({
    url: '/da/daAssetOperateLog',
    method: 'post',
    data: data
  })
}

// 修改数据资产操作记录
export function updateDaAssetOperateLog(data) {
  return request({
    url: '/da/daAssetOperateLog',
    method: 'put',
    data: data
  })
}

// 删除数据资产操作记录
export function delDaAssetOperateLog(id) {
  return request({
    url: '/da/daAssetOperateLog/' + id,
    method: 'delete'
  })
}
// 回滚
export function rollBack(id) {
  return request({
    url: `/da/daAssetOperateLog/rollBack/${id}`,
    method: 'post'
  })
}


// 修改记录
export function getDaAssetList(query) {
  return request({
    url: '/da/daAssetOperateLog/queryDaAssetOperateLogPage',
    method: 'get',
    params: query
  })
}
// // 回滚
// export function rollBack(id) {
//   return request({
//     url: `/da/daAssetOperateLog/rollBack/${id}`,
//     method: 'get',
//   })
// }