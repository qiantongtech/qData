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

// Query data element list
export function listDpDataElem(query) {
    return request({
        url: '/dp/dataElem/list',
        method: 'get',
        params: query
    });
}

// Query data element list
export function getDpDataElemList(query) {
    return request({
        url: '/dp/dataElem/getDpDataElemList',
        method: 'get',
        params: query
    });
}

// Query data element details
export function getDpDataElem(id) {
    return request({
        url: '/dp/dataElem/' + id,
        method: 'get'
    });
}

// Add new data element
export function addDpDataElem(data) {
    return request({
        url: '/dp/dataElem',
        method: 'post',
        data: data
    });
}

// Modify data element
export function updateDpDataElem(data) {
    return request({
        url: '/dp/dataElem',
        method: 'put',
        data: data
    });
}
// Modify data element
export function updateStatusDpDataElem(id, status) {
    return request({
        url: `/dp/dataElem/updateStatus/${id}/${status}`,
        method: 'post'
    });
}

// delete data element
export function delDpDataElem(id) {
    return request({
        url: '/dp/dataElem/' + id,
        method: 'delete'
    });
}
// Query the data element code mapping list
export function listDpCodeMap(query) {
    return request({
        url: '/dp/codeMap/list',
        method: 'get',
        params: query
    });
}

// Query data element code mapping details
export function getDpCodeMap(id) {
    return request({
        url: '/dp/codeMap/' + id,
        method: 'get'
    });
}

// Added data element code mapping
export function addDpCodeMap(data) {
    return request({
        url: '/dp/codeMap',
        method: 'post',
        data: data
    });
}

// Modify data element code mapping
export function updateDpCodeMap(data) {
    return request({
        url: '/dp/codeMap',
        method: 'put',
        data: data
    });
}

// Delete data metacode mapping
export function delDpCodeMap(id) {
    return request({
        url: '/dp/codeMap/' + id,
        method: 'delete'
    });
}
// Query the data metadata asset related information list
export function listDpDataElemAssetRel(query) {
    return request({
        url: '/dp/dataElemAssetRel/list',
        method: 'get',
        params: query
    });
}

// Query data metadata asset related information details
export function getDpDataElemAssetRel(id) {
    return request({
        url: '/dp/dataElemAssetRel/' + id,
        method: 'get'
    });
}

// Added data metadata asset related information
export function addDpDataElemAssetRel(data) {
    return request({
        url: '/dp/dataElemAssetRel',
        method: 'post',
        data: data
    });
}

// Modify data metadata asset related information
export function updateDpDataElemAssetRel(data) {
    return request({
        url: '/dp/dataElemAssetRel',
        method: 'put',
        data: data
    });
}

// Delete data metadata asset association information
export function delDpDataElemAssetRel(id) {
    return request({
        url: '/dp/dataElemAssetRel/' + id,
        method: 'delete'
    });
}
// Query the data element code list
export function listDpDataElemCode(query) {
    return request({
        url: '/dp/dataElemCode/list',
        method: 'get',
        params: query
    });
}

// Query data element code details
export function getDpDataElemCode(id) {
    return request({
        url: '/dp/dataElemCode/' + id,
        method: 'get'
    });
}

// Add data element code
export function addDpDataElemCode(data) {
    return request({
        url: '/dp/dataElemCode',
        method: 'post',
        data: data
    });
}

// Modify data element code
export function updateDpDataElemCode(data) {
    return request({
        url: '/dp/dataElemCode',
        method: 'put',
        data: data
    });
}

// Delete data element code
export function delDpDataElemCode(id) {
    return request({
        url: '/dp/dataElemCode/' + id,
        method: 'delete'
    });
}

//Verify source code values
export function validateCodeValue(params) {
    return request({
        url: '/dp/dataElemCode/validateCodeValue',
        method: 'get',
        params
    });
}
// Query the list of data metadata rule association information
export function listDpDataElemRuleRel(query) {
    return request({
        url: '/dp/dataElemRuleRel/list',
        method: 'get',
        params: query
    })
}

// Query detailed data metadata rule association information
export function getDpDataElemRuleRel(id) {
    return request({
        url: '/dp/dataElemRuleRel/' + id,
        method: 'get'
    })
}

// Added data metadata rule association information
export function addDpDataElemRuleRel(data) {
    return request({
        url: '/dp/dataElemRuleRel',
        method: 'post',
        data: data
    })
}

// Modify data metadata rule association information
export function updateDpDataElemRuleRel(data) {
    return request({
        url: '/dp/dataElemRuleRel',
        method: 'put',
        data: data
    })
}

// Delete data metadata rule association information
export function delDpDataElemRuleRel(id) {
    return request({
        url: '/dp/dataElemRuleRel/' + id,
        method: 'delete'
    })
}

// Save associated information
export function save(dataElemId, ruleType, data) {
    return request({
        url: `/dp/dataElemRuleRel/save/${dataElemId}/${ruleType}`,
        method: 'post',
        data
    })
}

// Data source cleaning audit rules
export function dpDataElemRuleRel(data) {
    return request({
        url: '/dp/dataElemRuleRel',
        method: 'post',
        data: data
    });
}
// Data source cleaning audit rules modification
export function putDpDataElemRuleRel(data) {
    return request({
        url: '/dp/dataElemRuleRel',
        method: 'put',
        data: data
    });
}
// Data source cleaning audit rules deletion
export function DlEPutDpDataElemRuleRel(id) {
    return request({
        url: '/dp/dataElemRuleRel/' + id,
        method: 'DELETE',
    });
}

// Data Integration Query
export function listDpDataElemRuleRelV2(query) {
    return request({
        url: '/da/asset/listRelRule/v2',
        method: 'get',
        params: query
    })
}

