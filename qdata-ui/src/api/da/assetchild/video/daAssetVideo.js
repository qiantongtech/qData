import request from '@/utils/request'

// 查询数据资产-视频数据列表
export function listDaAssetVideo(query) {
  return request({
    url: '/da/daAssetVideo/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产-视频数据详细
export function getDaAssetVideo(id) {
  return request({
    url: '/da/daAssetVideo/' + id,
    method: 'get'
  })
}

// 新增数据资产-视频数据
export function addDaAssetVideo(data) {
  return request({
    url: '/da/daAssetVideo',
    method: 'post',
    data: data
  })
}

// 修改数据资产-视频数据
export function updateDaAssetVideo(data) {
  return request({
    url: '/da/daAssetVideo',
    method: 'put',
    data: data
  })
}

// 删除数据资产-视频数据
export function delDaAssetVideo(id) {
  return request({
    url: '/da/daAssetVideo/' + id,
    method: 'delete'
  })
}
