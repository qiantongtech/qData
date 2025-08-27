import request from '@/utils/request'

// 查询清洗规则类目列表
export function listAttCleanCat(query) {
  return request({
    url: '/att/attCleanCat/list',
    method: 'get',
    params: query
  })
}

// 查询清洗规则类目详细
export function getAttCleanCat(ID) {
  return request({
    url: '/att/attCleanCat/' + ID,
    method: 'get'
  })
}

// 新增清洗规则类目
export function addAttCleanCat(data) {
  return request({
    url: '/att/attCleanCat',
    method: 'post',
    data: data
  })
}

// 修改清洗规则类目
export function updateAttCleanCat(data) {
  return request({
    url: '/att/attCleanCat',
    method: 'put',
    data: data
  })
}

// 删除清洗规则类目
export function delAttCleanCat(ID) {
  return request({
    url: '/att/attCleanCat/' + ID,
    method: 'delete'
  })
}
