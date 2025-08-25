import request from '@/utils/request'

// 查询数据质量任务-稽查对象列表
export function listDppQualityTaskObj(query) {
  return request({
    url: '/dpp/DppQualityTaskObj/list',
    method: 'get',
    params: query
  })
}

// 查询数据质量任务-稽查对象详细
export function getDppQualityTaskObj(id) {
  return request({
    url: '/dpp/DppQualityTaskObj/' + id,
    method: 'get'
  })
}

// 新增数据质量任务-稽查对象
export function addDppQualityTaskObj(data) {
  return request({
    url: '/dpp/DppQualityTaskObj',
    method: 'post',
    data: data
  })
}

// 修改数据质量任务-稽查对象
export function updateDppQualityTaskObj(data) {
  return request({
    url: '/dpp/DppQualityTaskObj',
    method: 'put',
    data: data
  })
}

// 删除数据质量任务-稽查对象
export function delDppQualityTaskObj(id) {
  return request({
    url: '/dpp/DppQualityTaskObj/' + id,
    method: 'delete'
  })
}
