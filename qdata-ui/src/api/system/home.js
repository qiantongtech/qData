import request from '@/utils/request.js';

// 查询首页参数
export function homeList(query) {
  console.log('query: ', query);
  return request({
    url: '/home',
    method: 'get',
    params: query
  })
}

