import request from '@/utils/request'

// 查询数据质量任务列表
export function listDppQualityTask(query) {
  return request({
    url: '/dpp/DppQualityTask/list',
    method: 'get',
    params: query
  })
}

// 查询数据质量任务详细
export function getDppQualityTask(id) {
  console.log("🚀 ~ getDppQualityTask ~ id:", id)
  return request({
    url: '/dpp/DppQualityTask/' + id,
    method: 'get'
  })
}

// 新增数据质量任务
export function addDppQualityTask(data) {
  return request({
    url: '/dpp/DppQualityTask',
    method: 'post',
    data: data
  })
}

// 修改数据质量任务
export function updateDppQualityTask(data) {
  return request({
    url: '/dpp/DppQualityTask',
    method: 'put',
    data: data
  })
}

// 删除数据质量任务
export function delDppQualityTask(id) {
  return request({
    url: '/dpp/DppQualityTask/' + id,
    method: 'delete'
  })
}
