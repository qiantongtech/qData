import request from '@/utils/request.js';

// Query table metadata list
export function listTable(query) {
    return request({
        url: '/mc/table/list',
        method: 'get',
        params: query
    });
}

// Query table metadata details
export function getTable(id) {
    return request({
        url: '/mc/table/' + id,
        method: 'get'
    });
}

// Add table metadata
export function addTable(data) {
    return request({
        url: '/mc/table',
        method: 'post',
        data: data
    });
}

// Modify table metadata
export function updateTable(data) {
    return request({
        url: '/mc/table',
        method: 'put',
        data: data
    });
}

// Delete table metadata
export function delTable(id) {
    return request({
        url: '/mc/table/' + id,
        method: 'delete'
    });
}

// Modify table metadata status
export function updateTableStatus(data) {
    return request({
        url: '/mc/table/toggle',
        method: 'post',
        data
    });
}

// Staging table metadata
export function draftTable(data) {
    return request({
        url: '/mc/table/draft',
        method: 'post',
        data: data
    });
}

// Get the deleteable columns of table metadata
export function batchDeleteCheck(id) {
    return request({
        url: '/mc/table/batchDeleteCheck/' + id,
        method: 'get'
    });
}

