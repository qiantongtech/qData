import request from '@/utils/request.js'

// Query the standard data element category management list
export function listElemCat(query) {
    return request({
        url: '/dg/dataElemCat/list',
        method: 'get',
        params: query
    })
}

// Query standard data element category management details
export function getElemCat(id) {
    return request({
        url: '/dg/dataElemCat/' + id,
        method: 'get'
    })
}

// Added standard data element category management
export function addElemCat(data) {
    return request({
        url: '/dg/dataElemCat',
        method: 'post',
        data: data
    })
}

// Modify standard data element category management
export function updateElemCat(data) {
    return request({
        url: '/dg/dataElemCat',
        method: 'put',
        data: data
    })
}

// Delete standard data element category management
export function delElemCat(id) {
    return request({
        url: '/dg/dataElemCat/' + id,
        method: 'delete'
    })
}
// Data element classification Batch deletion verification
export function batchDeleteCheck(ids) {
    return request({
        url: '/dg/dataElemCat/batchDeleteCheck/' + ids,
        method: 'get'
    })
}