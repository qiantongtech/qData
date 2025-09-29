import request from '@/utils/request'

// 查询清洗规则列表
export function listAttCleanRule(query) {
  return request({
    url: '/att/cleanRule/list',
    method: 'get',
    params: query
  })
}

// 查询清洗规则详细
export function getAttCleanRule(id) {
  return request({
    url: '/att/cleanRule/' + id,
    method: 'get'
  })
}

// 新增清洗规则
export function addAttCleanRule(data) {
  return request({
    url: '/att/cleanRule',
    method: 'post',
    data: data
  })
}

// 修改清洗规则
export function updateAttCleanRule(data) {
  return request({
    url: '/att/cleanRule',
    method: 'put',
    data: data
  })
}

// 删除清洗规则
export function delAttCleanRule(id) {
  return request({
    url: '/att/cleanRule/' + id,
    method: 'delete'
  })
}

// tree
export function treeAttCleanRule(params) {
  return request({
    url: '/att/cleanRule/tree',
    method: 'get',
    params
  })
}

// 数据集成用到的 清洗规则
export function listAll(params) {
  return request({
    url: '/att/cleanRule/listAll',
    method: 'get',
    params
  })
}
