import request from '@/utils/request'

// 查询数据质量任务-评测规则列表
export function listDppQualityTaskEvaluate(query) {
  return request({
    url: '/dpp/DppQualityTaskEvaluate/list',
    method: 'get',
    params: query
  })
}

// 查询数据质量任务-评测规则详细
export function getDppQualityTaskEvaluate(id) {
  return request({
    url: '/dpp/DppQualityTaskEvaluate/' + id,
    method: 'get'
  })
}

// 新增数据质量任务-评测规则
export function addDppQualityTaskEvaluate(data) {
  return request({
    url: '/dpp/DppQualityTaskEvaluate',
    method: 'post',
    data: data
  })
}

// 修改数据质量任务-评测规则
export function updateDppQualityTaskEvaluate(data) {
  return request({
    url: '/dpp/DppQualityTaskEvaluate',
    method: 'put',
    data: data
  })
}

// 删除数据质量任务-评测规则
export function delDppQualityTaskEvaluate(id) {
  return request({
    url: '/dpp/DppQualityTaskEvaluate/' + id,
    method: 'delete'
  })
}
