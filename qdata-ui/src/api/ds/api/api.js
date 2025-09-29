import request from '@/utils/request';

// 查询API服务列表
export function listDsApi(query) {
    return request({
        url: '/ds/api/list',
        method: 'get',
        params: query
    });
}

// 查询API服务详细
export function getDsApi(ID) {
    return request({
        url: '/ds/api/' + ID,
        method: 'get'
    });
}

// 新增API服务
export function repeatFlag(data) {
    return request({
        url: '/ds/api/repeatFlag',
        method: 'post',
        data: data
    });
}

// 新增API服务
export function addDsApi(data) {
    return request({
        url: '/ds/api',
        method: 'post',
        data: data
    });
}

// sql解析
export function sqlParse(data) {
    return request({
        url: '/ds/api/sqlParse',
        method: 'post',
        data: data
    });
}

// sql解析
export function serviceTesting(data) {
    if (data.headerJson !== null && typeof data.headerJson === 'object') {
        data.headerJson = JSON.stringify(data.headerJson)
    }
    return request({
        url: '/ds/api/serviceTesting',
        method: 'post',
        data: data
    });
}

// sql解析
export function addDataApi(data) {
    return request({
        url: '/ds/api',
        method: 'post',
        data: data
    });
}

export function updateDataApi(data) {
    return request({
        url: '/ds/api',
        method: 'put',
        data: data
    });
}

// 修改API服务
export function updateDsApi(data) {
    return request({
        url: '/ds/api',
        method: 'put',
        data: data
    });
}

// 删除API服务
export function delDsApi(ID) {
    return request({
        url: '/ds/api/' + ID,
        method: 'delete'
    });
}

// 删除API服务
export function listDataTable(ID) {
    return request({
        url: '/ds/api/listDataTable' + ID,
        method: 'get'
    });
}

// 启用API服务
export function releaseDataApi(ID) {
    return request({
        url: '/ds/api/release/' + ID,
        method: 'get'
    });
}

// 停用API服务
export function cancelDataApi(ID) {
    return request({
        url: '/ds/api/cancel/' + ID,
        method: 'get'
    });
}
export function queryServiceForwarding(data) {
    return request({
        url: '/ds/api/queryServiceForwarding',
        method: 'post',
        data: data
    });
}

export function selectByName(data) {
    return request({
        url: '/ds/api/selectList?name='+data,
        method: 'get',
    });
}
