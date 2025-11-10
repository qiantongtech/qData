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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

import request from '@/utils/request';

// 查询数据资产列表
export function listDaAsset(query) {
    return request({
        url: '/da/asset/list',
        method: 'get',
        params: query
    });
}

// 查询数据资产列表
export function listDppAsset(query) {
    return request({
        url: '/da/asset/dpp/list',
        method: 'get',
        params: query
    });
}

// 查询数据资产列表
export function dppNoPageList(query) {
    return request({
        url: '/da/asset/dpp/noPage/list',
        method: 'get',
        params: query
    });
}
export function pageListByIds(query) {
    return request({
        url: '/da/asset/listAssetTag',
        method: 'get',
        params: query
    });
}

// 查询数据资产详细
export function getDaAsset(id) {
    return request({
        url: '/da/asset/' + id,
        method: 'get'
    });
}

// 新增数据资产
export function addDaAsset(data) {
    return request({
        url: '/da/asset',
        method: 'post',
        data: data
    });
}

// 新增数据资产
export function bindResources(data) {
    return request({
        url: '/da/asset/bindResources',
        method: 'post',
        data: data
    });
}
// 修改数据资产
export function updateDaAsset(data) {
    return request({
        url: '/da/asset',
        method: 'put',
        data: data
    });
}

// 删除数据资产
export function delDaAsset(id) {
    return request({
        url: '/da/asset/' + id,
        method: 'delete'
    });
}

// 更新数据接口
export function startDaDiscoveryTask(data) {
    return request({
        url: `/da/asset/startDaDiscoveryTask`,
        method: 'put',
        data: data
    });
}

// 文件列表
export function getFileList(query) {
    return request({
        url: '/da/dataSource/fileList',
        method: 'get',
        params: query
    });
}

// 文件列表
export function uploadFile(query) {
    return request({
        url: '/da/dataSource/file',
        method: 'post',
        params: query
    });
}

// 数据血缘
export function dataLineage(id) {
    return request({
        url: '/da/asset/dataLineage/' + id,
        method: 'get',
    });
}

