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

// 查询项目列表
export function listAttProject(query) {
    return request({
        url: '/att/project/list',
        method: 'get',
        params: query
    });
}

// 查询当前用户所属的项目列表
export function currentUser() {
    return request({
        url: '/att/project/currentUser/list',
        method: 'get'
    });
}

// 查询当前用户所属的项目列表
export function noProjectUser(query) {
    return request({
        url: '/att/project/noProjectUser/list',
        method: 'post',
        params: query
    });
}

// 查询项目详细
export function getAttProject(id) {
    return request({
        url: '/att/project/' + id,
        method: 'get'
    });
}

// 获取当前用户是非具备用户添加和项目管理员
export function addUserAndProject(id) {
    return request({
        url: '/att/project/addUserAndProject/' + id,
        method: 'get'
    });
}

// 修改状态
export function editProjectStatus(id, status) {
    return request({
        url: `/att/project/editProjectStatus/${id}/${status}`,
        method: 'get'
    });
}

// 新增项目
export function addAttProject(data) {
    return request({
        url: '/att/project',
        method: 'post',
        data: data
    });
}

// 修改项目
export function updateAttProject(data) {
    return request({
        url: '/att/project',
        method: 'put',
        data: data
    });
}

// 删除项目
export function delAttProject(id) {
    return request({
        url: '/att/project/' + id,
        method: 'delete'
    });
}
