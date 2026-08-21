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

import request from '@/utils/request'

/**
 * Query the list of data integration node instances
 */
export function listDppEtlNodeInstance(query) {
        return request({
                url: '/dpp/etlNodeInstance/list',
                method: 'get',
                params: query
        });
}

/** Query data development node instance statistics. */
export function getEtlNodeInstanceStatistics(query) {
        return request({
                url: '/dpp/etlNodeInstance/statistics',
                method: 'get',
                params: query
        });
}

/** Query formatted node instance log details. */
export function getLogByNodeInstanceId(query) {
        return request({
                url: '/dpp/etlNodeInstance/logDetail',
                method: 'get',
                params: query
        });
}

/**
 * Query data integration node instance details
 */
export function getDppEtlNodeInstance(id) {
        return request({
                url: `/dpp/etlNodeInstance/${id}`,
                method: 'get'
        });
}

/**
 * Add new data integration node instance
 */
export function addDppEtlNodeInstance(data) {
        return request({
                url: '/dpp/etlNodeInstance',
                method: 'post',
                data: data
        });
}

/**
 * Modify the data integration node instance
 */
export function updateDppEtlNodeInstance(data) {
        return request({
                url: '/dpp/etlNodeInstance',
                method: 'put',
                data: data
        });
}

/**
 * Delete a data integration node instance
 */
export function delDppEtlNodeInstance(id) {
        return request({
                url: `/dpp/etlNodeInstance/${id}`,
                method: 'delete'
        });
}

/**
 * View log details (fixed the typo of "Super View")
 */
export function logDetailCat(id) {
        return request({
                url: `/dpp/etlNodeInstance/log/${id}`,
                method: 'get'
        });
}
