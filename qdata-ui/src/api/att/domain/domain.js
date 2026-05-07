
import request from '@/utils/request';

// 查询业务域列表
export function listDomain(query) {
    return request({
        url: '/mc/domain/list',
        method: 'get',
        params: query
    });
}

// 查询业务域详细
export function getDomain(id) {
    return request({
        url: '/mc/domain/' + id,
        method: 'get'
    });
}

// 新增业务域
export function addDomain(data) {
    return request({
        url: '/mc/domain',
        method: 'post',
        data: data
    });
}

// 修改业务域
export function updateDomain(data) {
    return request({
        url: '/mc/domain',
        method: 'put',
        data: data
    });
}

// 删除业务域
export function delDomain(id) {
    return request({
        url: '/mc/domain/' + id,
        method: 'delete'
    });
}

// 修改业务域状态
export function updateDomainStatus(id, validFlag) {
    return request({
        url: '/mc/domain',
        method: 'put',
        data: { id, validFlag }
    });
}

// 获取业务域可删除的列
export function batchDeleteCheck(id) {
    return request({
        url: '/mc/domain/batchDeleteCheck/' + id,
        method: 'get'
    });
}
