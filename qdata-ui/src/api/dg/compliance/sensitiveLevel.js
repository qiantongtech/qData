import request from '@/utils/request.js';

// Query sensitivity level list
export function listDgSensitiveLevel(query) {
    return request({
        url: '/dg/sensitiveLevel/list',
        method: 'get',
        params: query
    });
}

// Query sensitivity level details
export function getDgSensitiveLevel(id) {
    return request({
        url: '/dg/sensitiveLevel/' + id,
        method: 'get'
    });
}

// Add sensitivity level
export function addDgSensitiveLevel(data) {
    return request({
        url: '/dg/sensitiveLevel',
        method: 'post',
        data: data
    });
}

// Modify status online/offline
export function updateStatus(id, status) {
    return request({
        url: `/dg/sensitiveLevel/updateStatus/${id}/${status}`,
        method: 'post'
    });
}

// Modify sensitivity level
export function updateDgSensitiveLevel(data) {
    return request({
        url: '/dg/sensitiveLevel',
        method: 'put',
        data: data
    });
}

// Delete sensitivity level
export function delDgSensitiveLevel(id) {
    return request({
        url: '/dg/sensitiveLevel/' + id,
        method: 'delete'
    });
}
