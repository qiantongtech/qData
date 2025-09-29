import request from '@/utils/request'

// 查询服务资源门户部门列表
export function listRpDept(query) {
  return request({
    url: '/rp/rpDept/list',
    method: 'get',
    params: query
  })
}

// 查询服务资源门户部门列表
export function listRpDeptList(query) {
  return request({
    url: '/rp/rpDept/deptList',
    method: 'get',
    params: query
  })
}

// 查询服务资源门户部门详细
export function getRpDept(deptId) {
  return request({
    url: '/rp/rpDept/' + deptId,
    method: 'get'
  })
}

// 新增服务资源门户部门
export function addRpDept(data) {
  return request({
    url: '/rp/rpDept',
    method: 'post',
    data: data
  })
}

// 修改服务资源门户部门
export function updateRpDept(data) {
  return request({
    url: '/rp/rpDept',
    method: 'put',
    data: data
  })
}

// 删除服务资源门户部门
export function delRpDept(deptId) {
  return request({
    url: '/rp/rpDept/' + deptId,
    method: 'delete'
  })
}
