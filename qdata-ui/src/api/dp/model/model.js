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

// 查询已发布逻辑模型列表
export function releaseList(query) {
    return request({
        url: '/dp/model/releaseList',
        method: 'get',
        params: query
    });
}

// 查询逻辑模型列表
export function listDpModel(query) {
    return request({
        url: '/dp/model/list',
        method: 'get',
        params: query
    });
}

// 查询逻辑模型详细
export function getDpModel(ID) {
    return request({
        url: '/dp/model/' + ID,
        method: 'get'
    });
}

// 新增逻辑模型
export function addDpModel(data) {
    return request({
        url: '/dp/model',
        method: 'post',
        data: data
    });
}
// 新增逻辑模型
export function dpModelColumn(data) {
    return request({
        url: '/dp/modelColumn/addList',
        method: 'post',
        data: data
    });
}
// 新增逻辑模型
export function updateDpModel(data) {
    return request({
        url: '/dp/model',
        method: 'put',
        data: data
    });
}
// 修改逻辑模型
// export function updateDpModel(data) {
//   return request({
//     url: '/dp/dpModelColumn/editList',
//     method: 'put',
//     data: data
//   })
// }

// 删除逻辑模型
export function delDpModel(ID) {
    return request({
        url: '/dp/model/' + ID,
        method: 'delete'
    });
}

// 删除逻辑模型字段
export function delDpModelColumn(ID) {
    return request({
        url: '/dp/model/columnAll/' + ID,
        method: 'delete'
    });
}
// 查询逻辑模型类目管理列表
export function listAttModelCat(query) {
    return request({
        url: '/att/modelCat/list',
        method: 'get',
        params: query
    });
}
// 查询树
export function getTreeData(query) {
    return request({
        url: '/dp/model/getTreeData',
        method: 'get',
        params: query
    });
}
// 获取 表信息
export function getDpModelColumnList(query) {
    return request({
        url: '/dp/modelColumn/getDpModelColumnList',
        method: 'get',
        params: query
    });
}
// 修改逻辑模型
export function updateDpModelColumn(data) {
    return request({
        url: '/dp/modelColumn/editList',
        method: 'put',
        data: data
    });
}
// 发布
export function createMaterializedTable(data) {
    return request({
        url: '/dp/modelMaterialized/createMaterializedTable',
        method: 'post',
        data: data
    });
}
// 数据连接名称
export function getDaDatasourceList(query) {
    return request({
        url: '/da/dataSource/getDaDatasourceList',
        method: 'get',
        params: query
    });
}
// 修改状态
export function updateStatusDpDataModel(id, status) {
    return request({
        url: `/dp/model/updateStatus/${id}/${status}`,
        method: 'post'
    });
}
//表
export function tableList(ID, limit) {
    const idVal = typeof ID === 'object' ? ID.datasourceId : ID
    const params = typeof ID === 'object' ? { tableName: ID.tableName, limit } : { limit }
    return request({
        url: '/da/dataSource/tableList/' + idVal,
        method: 'get',
        params
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

// 查询逻辑模型属性信息列表
export function listDpModelColumn(query) {
    return request({
        url: '/dp/modelColumn/list',
        method: 'get',
        params: query
    })
}

// 查询逻辑模型属性信息详细
export function getDpModelColumn(id) {
    return request({
        url: '/dp/modelColumn/' + id,
        method: 'get'
    })
}

// 新增逻辑模型属性信息
export function addDpModelColumn(data) {
    return request({
        url: '/dp/modelColumn',
        method: 'post',
        data: data
    })
}

// 修改逻辑模型属性信息
export function updateDpModelColumns(data) {
    return request({
        url: '/dp/modelColumn',
        method: 'put',
        data: data
    })
}

// 删除逻辑模型属性信息
export function delDpModelColumns(id) {
    return request({
        url: '/dp/modelColumn/' + id,
        method: 'delete'
    })
}
// 查询发布模型记录列表
export function listDpModelMaterialized(query) {
    return request({
        url: '/dp/modelMaterialized/list',
        method: 'get',
        params: query
    })
}

// 查询发布模型记录详细
export function getDpModelMaterialized(id) {
    return request({
        url: '/dp/modelMaterialized/' + id,
        method: 'get'
    })
}

// 新增发布模型记录
export function addDpModelMaterialized(data) {
    return request({
        url: '/dp/modelMaterialized',
        method: 'post',
        data: data
    })
}

// 修改发布模型记录
export function updateDpModelMaterialized(data) {
    return request({
        url: '/dp/modelMaterialized',
        method: 'put',
        data: data
    })
}

// 删除发布模型记录
export function delDpModelMaterialized(id) {
    return request({
        url: '/dp/modelMaterialized/' + id,
        method: 'delete'
    })
}
// 批量删除模型校验
export function batchDeleteCheck(id) {
    return request({
        url: '/dp/model/batchDeleteCheck/' + id,
        method: 'get'
    });
}
