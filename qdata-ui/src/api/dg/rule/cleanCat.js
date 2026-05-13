import request from '@/utils/request'

// 查询清洗规则类目管理列表
export function listCleanCat(query) {
  return request({
    url: '/dg/cleanCat/list',
    method: 'get',
    params: query
  })
}

// 查询清洗规则类目管理详细
export function getCleanCat(id) {
  return request({
    url: '/dg/cleanCat/' + id,
    method: 'get'
  })
}

// 新增清洗规则类目管理
export function addCleanCat(data) {
  return request({
    url: '/dg/cleanCat',
    method: 'post',
    data: data
  })
}

// 修改清洗规则类目管理
export function updateCleanCat(data) {
  return request({
    url: '/dg/cleanCat',
    method: 'put',
    data: data
  })
}

// 删除清洗规则类目管理
export function delCleanCat(id) {
  return request({
    url: '/dg/cleanCat/' + id,
    method: 'delete'
  })
}
// 批量删除清洗规则类目管理前的校验
export function batchDeleteCheck(ids) {
  return request({
    url: '/dg/cleanCat/batchDeleteCheck/' + ids,
    method: 'get'
  })
}
