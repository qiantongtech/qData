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
// 物化
export function createMaterializedTable(data) {
    return request({
        url: '/dp/modelMaterialized/createMaterializedTable',
        method: 'post',
        data: data
    });
}
// 数据库连接
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
export function tableList(ID) {
    console.log("🚀 ~ tableList ~ ID:", ID)
    return request({
        url: '/da/dataSource/tableList/' + ID,
        method: 'get'
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
// 查询物化模型记录列表
export function listDpModelMaterialized(query) {
    return request({
        url: '/dp/modelMaterialized/list',
        method: 'get',
        params: query
    })
}

// 查询物化模型记录详细
export function getDpModelMaterialized(id) {
    return request({
        url: '/dp/modelMaterialized/' + id,
        method: 'get'
    })
}

// 新增物化模型记录
export function addDpModelMaterialized(data) {
    return request({
        url: '/dp/modelMaterialized',
        method: 'post',
        data: data
    })
}

// 修改物化模型记录
export function updateDpModelMaterialized(data) {
    return request({
        url: '/dp/modelMaterialized',
        method: 'put',
        data: data
    })
}

// 删除物化模型记录
export function delDpModelMaterialized(id) {
    return request({
        url: '/dp/modelMaterialized/' + id,
        method: 'delete'
    })
}
