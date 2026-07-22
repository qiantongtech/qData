/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import request from '@/utils/request';

// Query API service list
export function checkApi() {
    return request({
        url: '/ds/api/checkApi',
        method: 'get'
    });
}

// 查询API服务列表
export function listDsApi(query) {
    return request({
        url: '/ds/api/list',
        method: 'get',
        params: query
    });
}

// Query API service details
export function getDsApi(ID) {
    return request({
        url: '/ds/api/' + ID,
        method: 'get'
    });
}

// Add new API service
export function repeatFlag(data) {
    return request({
        url: '/ds/api/repeatFlag',
        method: 'post',
        data: data
    });
}

// Add new API service
export function addDsApi(data) {
    return request({
        url: '/ds/api',
        method: 'post',
        data: data
    });
}

// sql parsing
export function sqlParse(data) {
    return request({
        url: '/ds/api/sqlParse',
        method: 'post',
        data: data
    });
}

// sql parsing
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

// sql parsing
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

// Modify API service
export function updateDsApi(data) {
    return request({
        url: '/ds/api',
        method: 'put',
        data: data
    });
}

// Delete API service
export function delDsApi(ID) {
    return request({
        url: '/ds/api/' + ID,
        method: 'delete'
    });
}

// Delete API service
export function listDataTable(ID) {
    return request({
        url: '/ds/api/listDataTable' + ID,
        method: 'get'
    });
}

// Enable API service
export function releaseDataApi(ID) {
    return request({
        url: '/ds/api/release/' + ID,
        method: 'get'
    });
}

// Disable API service
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
