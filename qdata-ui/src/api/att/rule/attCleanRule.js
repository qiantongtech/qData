import request from '@/utils/request'

// 查询清洗规则列表
export function listAttCleanRule(query) {
  return request({
    url: '/att/attCleanRule/list',
    method: 'get',
    params: query
  })
}

// 查询清洗规则详细
export function getAttCleanRule(id) {
  return request({
    url: '/att/attCleanRule/' + id,
    method: 'get'
  })
}

// 新增清洗规则
export function addAttCleanRule(data) {
  return request({
    url: '/att/attCleanRule',
    method: 'post',
    data: data
  })
}

// 修改清洗规则
export function updateAttCleanRule(data) {
  return request({
    url: '/att/attCleanRule',
    method: 'put',
    data: data
  })
}

// 删除清洗规则
export function delAttCleanRule(id) {
  return request({
    url: '/att/attCleanRule/' + id,
    method: 'delete'
  })
}

// tree
export function treeAttCleanRule(params) {
  return request({
    url: '/att/attCleanRule/tree',
    method: 'get',
    params
  })
}
