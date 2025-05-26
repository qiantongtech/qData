import request from '@/utils/request'

// 查询学生列表
export function listStudent(query) {
  return request({
    url: '/example/student/list',
    method: 'get',
    params: query
  })
}

// 查询学生详细
export function getStudent(id) {
  return request({
    url: '/example/student/' + id,
    method: 'get'
  })
}

// 新增学生
export function addStudent(data) {
  return request({
    url: '/example/student',
    method: 'post',
    data: data
  })
}

// 修改学生
export function updateStudent(data) {
  return request({
    url: '/example/student',
    method: 'put',
    data: data
  })
}

// 删除学生
export function delStudent(id) {
  return request({
    url: '/example/student/' + id,
    method: 'delete'
  })
}
