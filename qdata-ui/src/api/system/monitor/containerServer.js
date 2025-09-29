import request from '@/utils/request'

// 查询容器管理-服务器配置列表
export function listcontainerServer(query) {
  return request({
    url: '/system/containerServer/list',
    method: 'get',
    params: query
  })
}

// 查询容器管理-服务器配置详细
export function getcontainerServer(ID) {
  return request({
    url: '/system/containerServer/info',
    method: 'get',
    params: { id: ID }
  }).then(response=>{
      response.data.containerList?.forEach(i=>{
          if(i.CreatedAt) {
              i.CreatedAt=new Date(i.CreatedAt.replace(' CST', ''))
          }
      })
      return response;
  })
}


// 新增容器管理-服务器配置
export function addcontainerServer(data) {
  return request({
    url: '/system/containerServer',
    method: 'post',
    data: data
  })
}

// 修改容器管理-服务器配置
export function updatecontainerServer(data) {
  return request({
    url: '/system/containerServer',
    method: 'put',
    data: data
  })
}

// 删除容器管理-服务器配置
export function delcontainerServer(ID) {
  return request({
    url: '/system/containerServer/' + ID,
    method: 'delete'
  })
}
