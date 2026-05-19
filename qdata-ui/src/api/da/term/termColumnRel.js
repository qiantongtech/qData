import request from '@/utils/request.js'

export function addTermColumnRel(data) {
  return request({
    url: '/da/termColumnRel',
    method: 'post',
    data
  })
}
