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

// Query the list of published logical models
export function releaseList(query) {
    return request({
        url: '/dp/model/releaseList',
        method: 'get',
        params: query
    });
}

// Query logical model list
export function listDpModel(query) {
    return request({
        url: '/dp/model/list',
        method: 'get',
        params: query
    });
}

// Query logical model details
export function getDpModel(ID) {
    return request({
        url: '/dp/model/' + ID,
        method: 'get'
    });
}

// Add new logical model
export function addDpModel(data) {
    return request({
        url: '/dp/model',
        method: 'post',
        data: data
    });
}
// Add new logical model
export function dpModelColumn(data) {
    return request({
        url: '/dp/modelColumn/addList',
        method: 'post',
        data: data
    });
}
// Add new logical model
export function updateDpModel(data) {
    return request({
        url: '/dp/model',
        method: 'put',
        data: data
    });
}
// Modify logical model
// export function updateDpModel(data) {
//   return request({
//     url: '/dp/dpModelColumn/editList',
//     method: 'put',
//     data: data
//   })
// }

// Delete logical model
export function delDpModel(ID) {
    return request({
        url: '/dp/model/' + ID,
        method: 'delete'
    });
}

// Delete logical model fields
export function delDpModelColumn(ID) {
    return request({
        url: '/dp/model/columnAll/' + ID,
        method: 'delete'
    });
}
// Query the logical model category management list
export function listAttModelCat(query) {
    return request({
        url: '/att/modelCat/list',
        method: 'get',
        params: query
    });
}
// query tree
export function getTreeData(query) {
    return request({
        url: '/dp/model/getTreeData',
        method: 'get',
        params: query
    });
}
// Get table information
export function getDpModelColumnList(query) {
    return request({
        url: '/dp/modelColumn/getDpModelColumnList',
        method: 'get',
        params: query
    });
}
// Modify logical model
export function updateDpModelColumn(data) {
    return request({
        url: '/dp/modelColumn/editList',
        method: 'put',
        data: data
    });
}
// publish
export function createMaterializedTable(data) {
    return request({
        url: '/dp/modelMaterialized/createMaterializedTable',
        method: 'post',
        data: data
    });
}
// Data connection name
export function getDaDatasourceList(query) {
    return request({
        url: '/da/dataSource/getDaDatasourceList',
        method: 'get',
        params: query
    });
}
// Modify status
export function updateStatusDpDataModel(id, status) {
    return request({
        url: `/dp/model/updateStatus/${id}/${status}`,
        method: 'post'
    });
}
//table
export function tableList(ID, limit) {
    const idVal = typeof ID === 'object' ? ID.datasourceId : ID
    const params = typeof ID === 'object' ? { tableName: ID.tableName, limit } : { limit }
    return request({
        url: '/da/dataSource/tableList/' + idVal,
        method: 'get',
        params
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

// Query logical model attribute information list
export function listDpModelColumn(query) {
    return request({
        url: '/dp/modelColumn/list',
        method: 'get',
        params: query
    })
}

// Query logical model attribute information details
export function getDpModelColumn(id) {
    return request({
        url: '/dp/modelColumn/' + id,
        method: 'get'
    })
}

// Added logical model attribute information
export function addDpModelColumn(data) {
    return request({
        url: '/dp/modelColumn',
        method: 'post',
        data: data
    })
}

// Modify logical model attribute information
export function updateDpModelColumns(data) {
    return request({
        url: '/dp/modelColumn',
        method: 'put',
        data: data
    })
}

// Delete logical model attribute information
export function delDpModelColumns(id) {
    return request({
        url: '/dp/modelColumn/' + id,
        method: 'delete'
    })
}
// Query the release model record list
export function listDpModelMaterialized(query) {
    return request({
        url: '/dp/modelMaterialized/list',
        method: 'get',
        params: query
    })
}

// Query release model record details
export function getDpModelMaterialized(id) {
    return request({
        url: '/dp/modelMaterialized/' + id,
        method: 'get'
    })
}

// Added release model record
export function addDpModelMaterialized(data) {
    return request({
        url: '/dp/modelMaterialized',
        method: 'post',
        data: data
    })
}

// Modify release model records
export function updateDpModelMaterialized(data) {
    return request({
        url: '/dp/modelMaterialized',
        method: 'put',
        data: data
    })
}

// Delete release model record
export function delDpModelMaterialized(id) {
    return request({
        url: '/dp/modelMaterialized/' + id,
        method: 'delete'
    })
}
// Delete model verification in batches
export function batchDeleteCheck(id) {
    return request({
        url: '/dp/model/batchDeleteCheck/' + id,
        method: 'get'
    });
}
