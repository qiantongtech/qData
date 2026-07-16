import request from '@/utils/request'

// Query the list of cleaning rules
export function listDgRule(query) {
    return request({
        url: '/dg/cleanRule/list',
        method: 'get',
        params: query
    })
}

// Query detailed cleaning rules
export function getDgRule(id) {
    return request({
        url: '/dg/cleanRule/' + id,
        method: 'get'
    })
}

// Add cleaning rules
export function addDgRule(data) {
    return request({
        url: '/dg/cleanRule',
        method: 'post',
        data: data
    })
}

// Modify cleaning rules
export function updateDgRule(data) {
    return request({
        url: '/dg/cleanRule',
        method: 'put',
        data: data
    })
}

// Delete cleaning rules
export function delDgRule(id) {
    return request({
        url: '/dg/cleanRule/' + id,
        method: 'delete'
    })
}

// tree
export function treeDgRule(params) {
    return request({
        url: '/dg/cleanRule/tree',
        method: 'get',
        params
    })
}

// Cleaning rules used in data integration
export function listAll(params) {
    return request({
        url: '/dg/cleanRule/listAll',
        method: 'get',
        params
    })
}
