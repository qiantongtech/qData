import request from '@/utils/request'

// 查询数据资产类目管理列表
export function listAttAssetCat(query) {
  return request({
    url: '/att/assetCat/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产类目管理详细
export function getAttAssetCat(id) {
  return request({
    url: '/att/assetCat/' + id,
    method: 'get'
  })
}

// 新增数据资产类目管理
export function addAttAssetCat(data) {
  return request({
    url: '/att/assetCat',
    method: 'post',
    data: data
  })
}

// 修改数据资产类目管理
export function updateAttAssetCat(data) {
  return request({
    url: '/att/assetCat',
    method: 'put',
    data: data
  })
}

// 删除数据资产类目管理
export function delAttAssetCat(id) {
  return request({
    url: '/att/assetCat/' + id,
    method: 'delete'
  })
}
