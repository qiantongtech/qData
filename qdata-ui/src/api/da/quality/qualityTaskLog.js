import request from '@/utils/request'

// 查询数据质量日志列表
export function listDppQualityLog(query) {
  return request({
    url: '/dpp/qualityLog/list',
    method: 'get',
    params: query
  })
}

// 查询数据质量日志详细
export function getDppQualityLog(id) {
  return request({
    url: '/dpp/qualityLog/' + id,
    method: 'get'
  })
}

// 新增数据质量日志
export function addDppQualityLog(data) {
  return request({
    url: '/dpp/qualityTask',
    method: 'post',
    data: data
  })
}

// 修改数据质量日志
export function updateDppQualityLog(data) {
  return request({
    url: '/dpp/qualityLog',
    method: 'put',
    data: data
  })
}

// 删除数据质量日志
export function delDppQualityLog(id) {
  return request({
    url: '/dpp/qualityLog/' + id,
    method: 'delete'
  })
}
//发送消息
export function doSendMessage(id) {
  return request({
    url: '/dpp/qualityLog/sendMessage',
    method: 'POST',
    params: {id}
  })
}
// 日志数据质量维度统计
export function statisticsEvaluateOne(id) {
  return request({
    url: '/dpp/evaluateLog/statisticsEvaluateOne/' + id,
    method: 'get',
  })
}
// 日志详情   治理数据量变化趋势
export function statisticsEvaluateTow(query) {
  console.log("🚀 ~ statisticsEvaluateTow ~ query:", query)
  return request({
    url: '/dpp/evaluateLog/statisticsEvaluateTow',
    method: 'get',
    params: query
  })
}
//日志 规则列表
export function statisticsEvaluateTable(id) {
  return request({
    url: '/dpp/evaluateLog/statisticsEvaluateTable/' + id,
    method: 'get',
  })
}
//  错误数据
export function pageErrorData(query) {
  return request({
    url: '/dpp/evaluateLog/pageErrorData',
    method: 'get',
    params: query
  })
}
// 修改接口 数据、状态，都是这个接口
export function updateErrorData(data) {
  return request({
    url: 'dpp/evaluateLog/updateErrorData',
    method: 'post',
    data: data
  })
}

