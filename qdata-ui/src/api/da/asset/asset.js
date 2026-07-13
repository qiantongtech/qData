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

// Query data asset list
export function listDaAsset(query) {
    return request({
        url: '/da/asset/list',
        method: 'get',
        params: query
    });
}

// Query data asset list
export function listDppAsset(query) {
    return request({
        url: '/da/asset/dpp/list',
        method: 'get',
        params: query
    });
}

// Query data asset list
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

// Query data asset details
export function getDaAsset(id) {
    return request({
        url: '/da/asset/' + id,
        method: 'get'
    });
}

// Add new data assets
export function addDaAsset(data) {
    return request({
        url: '/da/asset',
        method: 'post',
        data: data
    });
}

// Add new data assets
export function bindResources(data) {
    return request({
        url: '/da/asset/bindResources',
        method: 'post',
        data: data
    });
}
// Modify data assets
export function updateDaAsset(data) {
    return request({
        url: '/da/asset',
        method: 'put',
        data: data
    });
}

// Delete data assets
export function delDaAsset(id) {
    return request({
        url: '/da/asset/' + id,
        method: 'delete'
    });
}

// Update data interface
export function startDaDiscoveryTask(data) {
    return request({
        url: `/da/asset/startDaDiscoveryTask`,
        method: 'put',
        data: data
    });
}

// file list
export function getFileList(query) {
    return request({
        url: '/da/dataSource/fileList',
        method: 'get',
        params: query
    });
}

// file list
export function uploadFile(query) {
    return request({
        url: '/da/dataSource/file',
        method: 'post',
        params: query
    });
}

// Data lineage
export function dataLineage(id) {
    return request({
        url: '/da/asset/dataLineage/' + id,
        method: 'get',
    });
}

// Get tree structure
export function getTreeData(query) {
    return request({
        url: '/da/asset/getTreeData',
        method: 'get',
        params: query
    });
}

// Batch publishing
export function batchPublish(data) {
    return request({
        url: '/da/asset/batchPublish',
        method: 'post',
        data: data
    });
}

