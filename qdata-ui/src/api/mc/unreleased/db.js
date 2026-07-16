import request from '@/utils/request.js';

// Query library metadata list
export function listDb(query) {
    return request({
        url: '/mc/db/list',
        method: 'get',
        params: query
    });
}

// Query library metadata details
export function getDb(id) {
    return request({
        url: '/mc/db/' + id,
        method: 'get'
    });
}

// Add new library metadata
export function addDb(data) {
    return request({
        url: '/mc/db',
        method: 'post',
        data: data
    });
}

// Modify library metadata
export function updateDb(data) {
    return request({
        url: '/mc/db',
        method: 'put',
        data: data
    });
}

// Delete library metadata
export function delDb(id) {
    return request({
        url: '/mc/unreleased/db/' + id,
        method: 'delete'
    });
}

// Modify library metadata status
export function updateDbStatus(data) {
    return request({
        url: '/mc/unreleased/db/toggle',
        method: 'post',
        data
    });
}

// Get the deleteable columns of library metadata
export function batchDeleteCheck(id) {
    return request({
        url: '/mc/unreleased/db/batchDeleteCheck/' + id,
        method: 'get'
    });
}

