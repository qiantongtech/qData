import request from '@/utils/request.js';

// Query field metadata list
export function listColumn(query) {
    return request({
        url: '/mc/column/list',
        method: 'get',
        params: query
    });
}

// Query field metadata details
export function getColumn(id) {
    return request({
        url: '/mc/column/' + id,
        method: 'get'
    });
}

// Add new field metadata
export function addColumn(data) {
    return request({
        url: '/mc/column',
        method: 'post',
        data: data
    });
}

// Modify field metadata
export function updateColumn(data) {
    return request({
        url: '/mc/column',
        method: 'put',
        data: data
    });
}

// Remove field metadata
export function delColumn(id) {
    return request({
        url: '/mc/column/' + id,
        method: 'delete'
    });
}

// Modify field metadata status
export function updateColumnStatus(data) {
    return request({
        url: '/mc/column/toggle',
        method: 'post',
        data
    });
}

// Staging field metadata
export function draftColumn(data) {
    return request({
        url: '/mc/column/draft',
        method: 'post',
        data: data
    });
}

// Get the deleteable columns of library metadata
export function batchDeleteCheck(id) {
    return request({
        url: '/mc/column/batchDeleteCheck/' + id,
        method: 'get'
    });
}
