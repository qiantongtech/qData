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

// Query data source list
export function listDaDatasource(query) {
    return request({
        url: '/da/dataSource/list',
        method: 'get',
        params: query
    });
}

export function daList(query) {
    return request({
        url: '/da/list',
        method: 'get',
        params: query
    });
}
// Query data source list
export function sqlParse(data) {
    return request({
        url: '/da/dataSource/sqlParse',
        method: 'post',
        data: data
    });
}

// Kafka is excluded from data integration and is the data source list of the current project
export function listDaDatasourceNoKafkaByProjectCode(query) {
    return request({
        url: '/da/dataSource/dppNoKafka/list',
        method: 'get',
        params: query
    });
}

// Query the data source list of the R&D module
export function listDaDatasourceByProjectCode(query) {
    return request({
        url: '/da/dataSource/dpp/list',
        method: 'get',
        params: query
    });
}

// Query the data source list of the R&D module
export function noDppAdd(query) {
    return request({
        url: '/da/dataSource/noDppAdd/list',
        method: 'get',
        params: query
    });
}

// Query data source list
export function getDaDatasourceList(query) {
    return request({
        url: '/da/dataSource/getDataSourceByAsset',
        method: 'get',
        params: query
    });
}

// Query data source details
export function getDaDatasource(id) {
    return request({
        url: '/da/dataSource/' + id,
        method: 'get'
    });
}

// Query data source details
export function clientsTest(id) {
    return request({
        url: '/da/dataSource/clientsTest/' + id,
        method: 'get'
    });
}

// Add new data source
export function addDaDatasource(data) {
    return request({
        url: '/da/dataSource',
        method: 'post',
        data: data
    });
}

// Modify data source
export function updateDaDatasource(data) {
    return request({
        url: '/da/dataSource',
        method: 'put',
        data: data
    });
}

// Delete data source
export function delDaDatasource(id) {
    return request({
        url: '/da/dataSource/' + id,
        method: 'delete'
    });
}

// Delete data source
export function removeDppOrDa(id, type) {
    return request({
        url: '/da/dataSource/' + id + '/' + type,
        method: 'delete'
    });
}

// Modify status
export function editDatasourceStatus(id, status) {
    return request({
        url: `/da/dataSource/editDatasourceStatus/${id}/${status}`,
        method: 'get'
    });
}

export function tableList(id) {
    return request({
        url: `/da/dataSource/tableList/${id}`,
        method: 'get',
    });
}

// table fields
export function columnsList(data) {
    return request({
        url: `/da/dataSource/columnsList`,
        method: 'post',
        data: data
    });
}

// Get table information based on id
export function getTablesByDataSourceId(query) {
    return request({
        url: '/da/asset/getTablesByDataSourceId',
        method: 'get',
        params: query
    });
}

// Get table information based on id
export function getColumnByAssetId(query) {
    return request({
        url: '/da/assetColumn/getColumnByAssetId',
        method: 'get',
        params: query
    });
}

// Query the data source list of the R&D module
export function executeSqlQuery(query) {
    return request({
        url: '/da/dataSource/executeSqlQuery',
        method: 'get',
        params: query
    });
}
// Data asset asset application list
export function getDaAssetRespList(query) {
    return request({
        url: '/da/asset/getDaAssetRespList',
        method: 'get',
        params: query
    });
}
