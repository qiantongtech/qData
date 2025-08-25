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


//检验接口
export function verifyInterfaceValue(query) {
  return request({
    url: '/dpp/DppQualityTaskEvaluate/verifyInterfaceValue',
    method: 'get',
    params: query
  })
}
//错误抽查功能
export function validationErrorDataSql(data) {
  return request({
    url: '/dpp/DppQualityTaskEvaluate/validationErrorDataSql',
    method: 'post',
    data: data
  })
}
// 成功抽查功能
export function validationValidDataSql(data) {
  return request({
    url: '/dpp/DppQualityTaskEvaluate/validationValidDataSql',
    method: 'post',
    data: data
  })
}
//执行一次
export function startDppQualityTask(id) {
  return request({
    url: `/dpp/DppQualityTask//startDppQualityTask/${id}`,
    method: 'put',
  })
}
// 任务开关
export function updateDppQualityTaskStatus(query) {
  return request({
    url: '/dpp/DppQualityTask/updateDppQualityTaskStatus',
    method: 'post',
    data: query
  })
}

// 调度周期

export function updateDaDiscoveryTaskCronExpression(query) {
  return request({
    url: '/dpp/DppQualityTask/updateDaDiscoveryTaskCronExpression',
    method: 'post',
    data: query
  })
}// 数据质量 查询资产质量详情

export function getQualityTaskAsset(query) {
  return request({
    url: '/dpp/DppQualityTask/getQualityTaskAsset',
    method: 'get',
    params: query
  });
}
// 数据质量 日志数据质量维度统计

export function statisticsEvaluateAssetOne(query) {
  return request({
    url: 'dpp/evaluateLog/statisticsEvaluateAssetOne',
    method: 'get',
    params: query
  });
}