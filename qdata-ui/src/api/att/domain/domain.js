
import request from '@/utils/request';

// Query business domain list
export function listDomain(query) {
    return request({
        url: '/mc/domain/list',
        method: 'get',
        params: query
    });
}

// Query business domain details
export function getDomain(id) {
    return request({
        url: '/mc/domain/' + id,
        method: 'get'
    });
}

// Add new business domain
export function addDomain(data) {
    return request({
        url: '/mc/domain',
        method: 'post',
        data: data
    });
}

// Modify business domain
export function updateDomain(data) {
    return request({
        url: '/mc/domain',
        method: 'put',
        data: data
    });
}

// Delete business domain
export function delDomain(id) {
    return request({
        url: '/mc/domain/' + id,
        method: 'delete'
    });
}

// Modify business domain status
export function updateDomainStatus(id, validFlag) {
    return request({
        url: '/mc/domain',
        method: 'put',
        data: { id, validFlag }
    });
}

// Get the deleteable columns of the business domain
export function batchDeleteCheck(id) {
    return request({
        url: '/mc/domain/batchDeleteCheck/' + id,
        method: 'get'
    });
}
