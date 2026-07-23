import request from '@/utils/request'

// Query the cleaning rule category management list
export function listCleanCat(query) {
  return request({
    url: '/dg/cleanCat/list',
    method: 'get',
    params: query
  })
}

// Query cleaning rule category management details
export function getCleanCat(id) {
  return request({
    url: '/dg/cleanCat/' + id,
    method: 'get'
  })
}

// Added cleaning rule category management
export function addCleanCat(data) {
  return request({
    url: '/dg/cleanCat',
    method: 'post',
    data: data
  })
}

// Modify cleaning rule category management
export function updateCleanCat(data) {
  return request({
    url: '/dg/cleanCat',
    method: 'put',
    data: data
  })
}

// Delete cleaning rule category management
export function delCleanCat(id) {
  return request({
    url: '/dg/cleanCat/' + id,
    method: 'delete'
  })
}
// Verification before batch deletion of cleaning rule category management
export function batchDeleteCheck(ids) {
  return request({
    url: '/dg/cleanCat/batchDeleteCheck/' + ids,
    method: 'get'
  })
}
