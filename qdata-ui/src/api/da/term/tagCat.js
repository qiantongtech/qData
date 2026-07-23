
import request from '@/utils/request.js'

// Query term label category management list
export function listAttTagCat(query) {
    return request({
        url: '/da/termCat/list',
        method: 'get',
        params: query
    })
}

// Query term label category management details
export function getAttTagCat(id) {
    return request({
        url: '/da/termCat/' + id,
        method: 'get'
    })
}

// Added term label category management
export function addAttTagCat(data) {
    return request({
        url: '/da/termCat',
        method: 'post',
        data: data
    })
}

// Modify term label category management
export function updateAttTagCat(data) {
    return request({
        url: '/da/termCat',
        method: 'put',
        data: data
    })
}

// Delete term tag category management
export function delAttTagCat(id) {
    return request({
        url: '/da/termCat/' + id,
        method: 'delete'
    })
}
