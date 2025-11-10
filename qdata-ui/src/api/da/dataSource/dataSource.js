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

// 查询数据源列表
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
// 查询数据源列表
export function sqlParse(data) {
    return request({
        url: '/da/dataSource/sqlParse',
        method: 'post',
        data: data
    });
}

// 数据集成中排除Kafka并且是当前项目的数据源列表
export function listDaDatasourceNoKafkaByProjectCode(query) {
    return request({
        url: '/da/dataSource/dppNoKafka/list',
        method: 'get',
        params: query
    });
}

// 查询研发模块的数据源列表
export function listDaDatasourceByProjectCode(query) {
    return request({
        url: '/da/dataSource/dpp/list',
        method: 'get',
        params: query
    });
}

// 查询研发模块的数据源列表
export function noDppAdd(query) {
    return request({
        url: '/da/dataSource/noDppAdd/list',
        method: 'get',
        params: query
    });
}

// 查询数据源列表
export function getDaDatasourceList(query) {
    return request({
        url: '/da/dataSource/getDataSourceByAsset',
        method: 'get',
        params: query
    });
}

// 查询数据源详细
export function getDaDatasource(id) {
    return request({
        url: '/da/dataSource/' + id,
        method: 'get'
    });
}

// 查询数据源详细
export function clientsTest(id) {
    return request({
        url: '/da/dataSource/clientsTest/' + id,
        method: 'get'
    });
}

// 新增数据源
export function addDaDatasource(data) {
    return request({
        url: '/da/dataSource',
        method: 'post',
        data: data
    });
}

// 修改数据源
export function updateDaDatasource(data) {
    return request({
        url: '/da/dataSource',
        method: 'put',
        data: data
    });
}

// 删除数据源
export function delDaDatasource(id) {
    return request({
        url: '/da/dataSource/' + id,
        method: 'delete'
    });
}

// 删除数据源
export function removeDppOrDa(id, type) {
    return request({
        url: '/da/dataSource/' + id + '/' + type,
        method: 'delete'
    });
}

// 修改状态
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

// 表字段
export function columnsList(data) {
    return request({
        url: `/da/dataSource/columnsList`,
        method: 'post',
        data: data
    });
}

// 根据id获取表信息
export function getTablesByDataSourceId(query) {
    return request({
        url: '/da/asset/getTablesByDataSourceId',
        method: 'get',
        params: query
    });
}

// 根据id获取表信息
export function getColumnByAssetId(query) {
    return request({
        url: '/da/assetColumn/getColumnByAssetId',
        method: 'get',
        params: query
    });
}

// 查询研发模块的数据源列表
export function executeSqlQuery(query) {
    return request({
        url: '/da/dataSource/executeSqlQuery',
        method: 'get',
        params: query
    });
}
// 数据资产资产申请列表
export function getDaAssetRespList(query) {
    return request({
        url: '/da/asset/getDaAssetRespList',
        method: 'get',
        params: query
    });
}
