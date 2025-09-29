import request from '@/utils/request'

// 查询数据发现任务类目管理列表
export function listAttDiscoverTaskCat(query) {
  return request({
    url: '/att/discoverTaskCat/list',
    method: 'get',
    params: query
  })
}

// 查询数据发现任务类目管理详细
export function getAttDiscoverTaskCat(id) {
  return request({
    url: '/att/discoverTaskCat/' + id,
    method: 'get'
  })
}

// 新增数据发现任务类目管理
export function addAttDiscoverTaskCat(data) {
  return request({
    url: '/att/discoverTaskCat',
    method: 'post',
    data: data
  })
}

// 修改数据发现任务类目管理
export function updateAttDiscoverTaskCat(data) {
  return request({
    url: '/att/discoverTaskCat',
    method: 'put',
    data: data
  })
}

// 删除数据发现任务类目管理
export function delAttDiscoverTaskCat(id) {
  return request({
    url: '/att/discoverTaskCat/' + id,
    method: 'delete'
  })
}
