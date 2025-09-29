import request from '@/utils/request'

// 查询数据集成任务类目管理列表
export function listAttTaskCat(query) {
  return request({
    url: '/att/taskCat/list',
    method: 'get',
    params: query
  })
}

// 查询数据集成任务类目管理详细
export function getAttTaskCat(id) {
  return request({
    url: '/att/taskCat/' + id,
    method: 'get'
  })
}

// 新增数据集成任务类目管理
export function addAttTaskCat(data) {
  return request({
    url: '/att/taskCat',
    method: 'post',
    data: data
  })
}

// 修改数据集成任务类目管理
export function updateAttTaskCat(data) {
  return request({
    url: '/att/taskCat',
    method: 'put',
    data: data
  })
}

// 删除数据集成任务类目管理
export function delAttTaskCat(id) {
  return request({
    url: '/att/taskCat/' + id,
    method: 'delete'
  })
}
