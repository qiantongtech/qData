import request from '@/utils/request'

// 查询数据集成任务列表
export function listDppEtlTask(query) {
  return request({
    url: '/dpp/dppEtlTask/getDppEtlTaskPage',
    method: 'get',
    params: query
  })
}

// 查询数据集成任务详细
export function getDppEtlTask(id) {
  return request({
    url: '/dpp/dppEtlTask/' + id,
    method: 'get'
  })
}

// 新增数据集成任务
export function addDppEtlTask(data) {
  return request({
    url: '/dpp/dppEtlTask',
    method: 'post',
    data: data
  })
}

// 修改数据集成任务
export function updateDppEtlTask(data) {
  return request({
    url: '/dpp/dppEtlTask',
    method: 'put',
    data: data
  })
}

// 删除数据集成任务
export function delDppEtlTask(id) {
  console.log("🚀 ~ delDppEtlTask ~ id:", id)
  return request({
    url: '/dpp/dppEtlTask/' + id,
    method: 'delete'
  })
}
// 表列表
// export function getTablesByDataSourceId(query) {
//   return request({
//     url: '/da/daAsset/getTablesByDataSourceId',
//     method: 'get',
//     params: query
//   })
// }
export function getTablesByDataSourceId(ID) {
  console.log("🚀 ~ tableList ~ ID:",)
  return request({
    url: '/da/daDatasource/tableList/' + ID.datasourceId,
    method: 'get'
  });
}
// 表字段
export function getColumnByAssetId(data) {
  return request({
    url: `/da/daDatasource/columnsAsAssetColumnList`,
    method: 'post',
    data: data
  });
}
// 表字段
// export function getColumnByAssetId(query) {
//   return request({
//     url: '/da/daAssetColumn/getColumnByAssetId',
//     method: 'get',
//     params: query
//   })
// }
// code获取
export function getNodeUniqueKey(query) {
  return request({
    url: '/dpp/dppEtlTask/getNodeUniqueKey',
    method: 'get',
    params: query
  })
}
// code获取
export function getCleaningRuleTree(query) {
  return request({
    url: '/att/attCleanRule/getCleaningRuleTree',
    method: 'get',
    params: query
  })
}
// code获取
export function createTaskTempTable(data) {
  return request({
    url: '/da/daDatasource/createTaskTempTable',
    method: 'post',
    data: data
  })
}
// 新增接口 dag
export function createProcessDefinition(data) {
  return request({
    url: '/dpp/dppEtlTask/createProcessDefinitionEx',
    method: 'post',
    data: data
  })
}
// 新增接口 数据集成dag
export function createProcessDefinitions(data) {
  return request({
    // url: '/dpp/dppEtlTask/createProcessDefinitionEx',
    url: '/dpp/dppEtlTask/createEtlTask',
    method: 'post',
    data: data
  })
}
// 上线下线
export function updateReleaseTask(data) {
  return request({
    url: '/dpp/dppEtlTask/updateReleaseTask',
    method: 'post',
    data: data
  })
}
// 上线下线 調度
export function updateReleaseSchedule(data) {
  return request({
    url: '/dpp/dppEtlTask/updateReleaseSchedule',
    method: 'post',
    data: data
  })
}
// 上线下线 任務
export function updateReleaseJobTask(data) {
  return request({
    url: '/dpp/dppEtlTask/updateReleaseJobTask',
    method: 'post',
    data: data
  })
}


// 详情
export function dppEtlTask(id) {
  return request({
    url: '/dpp/dppEtlTask/updateQuery/' + id,
    method: 'get',

  })
}


// 修改
export function updateProcessDefinition(data) {
  return request({
    url: '/dpp/dppEtlTask/updateProcessDefinition',
    method: 'post',
    data: data
  })
}
// 数据集成修改
export function updateProcessDefinitions(data) {
  return request({
    // url: '/dpp/dppEtlTask/updateProcessDefinition',
    url: '/dpp/dppEtlTask/updateEtlTask',
    method: 'post',
    data: data
  })
}
// 修改调度
export function releaseTaskCrontab(data) {
  return request({
    url: '/dpp/dppEtlTask/releaseTaskCrontab',
    method: 'post',
    data: data
  })
}

// 查询作业任务 树形
export function getDppEtlTaskListTree(query) {
  return request({
    url: '/dpp/dppEtlTask/getDppEtlTaskListTree',
    method: 'get',
    params: query
  })
}

// 解析exel
export function getExcelColumn(data) {
  return request({
    url: '/common/getExcelColumn ',
    method: 'post',
    data: data
  })
}
//  

// 表code获取
export function createTaskTempTableByExcel(data) {
  return request({
    url: '/da/daDatasource/createTaskTempTableByExcel',
    method: 'post',
    data: data
  })
}

export function createTaskTempTableByExcel2(data) {
  return request({
    url: 'da/daDatasource/createTaskTempTable/2',
    method: 'post',
    data: data
  })
}

export function getDaDatasourceList(query) {
  return request({
    url: '/da/daDatasource/getDaDatasourceList',
    method: 'get',
    params: query
  })
}
// jiexi csv
export function getCsvColumn(data) {
  return request({
    url: '/common/getCsvColumn',
    method: 'post',
    data: data
  })
}

// 数据研发 执行一次
export function startDppEtlTask(data) {
  return request({
    url: '/dpp/dppEtlTask/startDppEtlTask/' + data,
    method: 'put',
  })
}
