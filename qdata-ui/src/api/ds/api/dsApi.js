import request from '@/utils/request';

// 查询API服务列表
export function listDsApi(query) {
    return request({
        url: '/ds/dsApi/list',
        method: 'get',
        params: query
    });
}

// 查询API服务详细
export function getDsApi(ID) {
    return request({
        url: '/ds/dsApi/' + ID,
        method: 'get'
    });
}

// 新增API服务
export function repeatFlag(data) {
    return request({
        url: '/ds/dsApi/repeatFlag',
        method: 'post',
        data: data
    });
}

// 新增API服务
export function addDsApi(data) {
    return request({
        url: '/ds/dsApi',
        method: 'post',
        data: data
    });
}

// sql解析
export function sqlParse(data) {
    return request({
        url: '/ds/dsApi/sqlParse',
        method: 'post',
        data: data
    });
}

// sql解析
export function serviceTesting(data) {
    return request({
        url: '/ds/dsApi/serviceTesting',
        method: 'post',
        data: data
    });
}

// sql解析
export function addDataApi(data) {
    return request({
        url: '/ds/dsApi',
        method: 'post',
        data: data
    });
}

export function updateDataApi(data) {
    return request({
        url: '/ds/dsApi',
        method: 'put',
        data: data
    });
}

// 修改API服务
export function updateDsApi(data) {
    return request({
        url: '/ds/dsApi',
        method: 'put',
        data: data
    });
}

// 删除API服务
export function delDsApi(ID) {
    return request({
        url: '/ds/dsApi/' + ID,
        method: 'delete'
    });
}

// 删除API服务
export function listDataTable(ID) {
    return request({
        url: '/ds/dsApi/listDataTable' + ID,
        method: 'get'
    });
}

// 启用API服务
export function releaseDataApi(ID) {
    return request({
        url: '/ds/dsApi/release/' + ID,
        method: 'get'
    });
}

// 停用API服务
export function cancelDataApi(ID) {
    return request({
        url: '/ds/dsApi/cancel/' + ID,
        method: 'get'
    });
}
export function queryServiceForwarding(data) {
    return request({
        url: '/ds/dsApi/queryServiceForwarding',
        method: 'post',
        data: data
    });
}
