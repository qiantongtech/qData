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
 */

import request from '@/utils/request'


/**
 * 查询数据集成节点实例列表
 */
export function listDppEtlNodeInstance(query) {
        return request({
                url: '/dpp/etlNodeInstance/list',
                method: 'get',
                params: query
        });
}

/**
 * 查询数据集成节点实例详细
 */
export function getDppEtlNodeInstance(id) {
        return request({
                url: `/dpp/etlNodeInstance/${id}`,
                method: 'get'
        });
}

/**
 * 新增数据集成节点实例
 */
export function addDppEtlNodeInstance(data) {
        return request({
                url: '/dpp/etlNodeInstance',
                method: 'post',
                data: data
        });
}

/**
 * 修改数据集成节点实例
 */
export function updateDppEtlNodeInstance(data) {
        return request({
                url: '/dpp/etlNodeInstance',
                method: 'put',
                data: data
        });
}

/**
 * 删除数据集成节点实例
 */
export function delDppEtlNodeInstance(id) {
        return request({
                url: `/dpp/etlNodeInstance/${id}`,
                method: 'delete'
        });
}

/**
 * 查看日志详情（修复了“超看”错别字）
 */
export function logDetailCat(id) {
        return request({
                url: `/dpp/etlNodeInstance/log/${id}`,
                method: 'get'
        });
}
