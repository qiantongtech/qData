import request from '@/utils/request'

// 查询示例部门列表
export function listDept(query) {
  return request({
    url: '/example/dept/list',
    method: 'get',
    params: query
  })
}

// 查询示例部门详细
export function getDept(id) {
  return request({
    url: '/example/dept/' + id,
    method: 'get'
  })
}

// 新增示例部门
export function addDept(data) {
  return request({
    url: '/example/dept',
    method: 'post',
    data: data
  })
}

// 修改示例部门
export function updateDept(data) {
  return request({
    url: '/example/dept',
    method: 'put',
    data: data
  })
}

// 删除示例部门
export function delDept(id) {
  return request({
    url: '/example/dept/' + id,
    method: 'delete'
  })
}
