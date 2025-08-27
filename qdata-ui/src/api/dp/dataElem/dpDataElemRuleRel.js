import request from '@/utils/request'

// 查询数据元数据规则关联信息列表
export function listDpDataElemRuleRel(query) {
  return request({
    url: '/dp/dpDataElemRuleRel/list',
    method: 'get',
    params: query
  })
}

// 查询数据元数据规则关联信息详细
export function getDpDataElemRuleRel(id) {
  return request({
    url: '/dp/dpDataElemRuleRel/' + id,
    method: 'get'
  })
}

// 新增数据元数据规则关联信息
export function addDpDataElemRuleRel(data) {
  return request({
    url: '/dp/dpDataElemRuleRel',
    method: 'post',
    data: data
  })
}

// 修改数据元数据规则关联信息
export function updateDpDataElemRuleRel(data) {
  return request({
    url: '/dp/dpDataElemRuleRel',
    method: 'put',
    data: data
  })
}

// 删除数据元数据规则关联信息
export function delDpDataElemRuleRel(id) {
  return request({
    url: '/dp/dpDataElemRuleRel/' + id,
    method: 'delete'
  })
}

// 保存关联信息
export function save(dataElemId, ruleType, data) {
  return request({
    url: `/dp/dpDataElemRuleRel/save/${dataElemId}/${ruleType}`,
    method: 'post',
    data
  })
}

// 数据源清洗 稽查规则
export function dpDataElemRuleRel(data) {
  return request({
    url: '/dp/dpDataElemRuleRel',
    method: 'post',
    data: data
  });
}
// 数据源清洗 稽查规则 修改
export function putDpDataElemRuleRel(data) {
  return request({
    url: '/dp/dpDataElemRuleRel',
    method: 'put',
    data: data
  });
}
// 数据源清洗 稽查规则 删除
export function DlEPutDpDataElemRuleRel(id) {
  return request({
    url: '/dp/dpDataElemRuleRel/' + id,
    method: 'DELETE',
  });
}

// 数据集成 查询 
export function listDpDataElemRuleRelV2(query) {
  return request({
    url: '/da/daAsset/listRelRule/v2',
    method: 'get',
    params: query
  })
}