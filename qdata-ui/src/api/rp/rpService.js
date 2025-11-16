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

import request from '@/utils/rpRequest';

// 左侧树
export function catApiList(query) {
    return request({
        url: '/rp/attApi/catApi/list',
        method: 'get',
        params: query
    });
}

// 列表
export function dsApiList(query) {
    return request({
        url: '/rp/dsApi/list',
        method: 'get',
        params: query
    });
}

// 列表详情
export function dsApiView(id) {
    return request({
        url: '/rp/dsApi/' + id,
        method: 'get'
    });
}

// 查询个人中心我的申请统计项
export function applyCount() {
    return request({
        url: '/rp/dsApi/applyCount',
        method: 'get'
    });
}

// 可申请的API列表
export function areaDict(data) {
    return request({
        url: '/rp/rpDict/areaDict',
        method: 'post',
        data: data
    });
}

export function applyAdd(data) {
    return request({
        url: '/da/apply',
        method: 'post',
        data: data
    });
}

export function applyEdit(data) {
    return request({
        url: '/da/apply',
        method: 'put',
        data: data
    });
}

// 查询API申请服务详细
export function getDsApiApply(id) {
    return request({
        url: '/da/apply/' + id,
        method: 'get'
    });
}

// 删除API申请服务
export function applyDel(id) {
    return request({
        url: '/da/apply/' + id,
        method: 'delete'
    });
}
