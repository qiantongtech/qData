/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
