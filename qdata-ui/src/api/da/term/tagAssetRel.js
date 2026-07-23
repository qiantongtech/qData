

import request from '@/utils/request.js'

// Query the list of associations between tags and terms
export function listAttTagAssetRel(query) {
  return request({
    url: '/att/tagAssetRel/list',
    method: 'get',
    params: query
  })
}

// Query the detailed relationship between tags and terms
export function getAttTagAssetRel(ID) {
  return request({
    url: '/att/tagAssetRel/' + ID,
    method: 'get'
  })
}

// Add new tags and term relationships
export function addAttTagAssetRel(data) {
  return request({
    url: '/att/tagAssetRel',
    method: 'post',
    data: data
  })
}

// Modify the relationship between labels and terms
export function updateAttTagAssetRel(data) {
  return request({
    url: '/att/tagAssetRel',
    method: 'put',
    data: data
  })
}

// Delete the association between tags and terms
export function delAttTagAssetRel(ID) {
  return request({
    url: '/att/tagAssetRel/' + ID,
    method: 'delete'
  })
}

export function delByTagIdAndAesstId(query) {
  return request({
    url: '/da/termColumnRel/delByTagIdAndAesstId',
    method: 'delete',
    params: query
  })
}

