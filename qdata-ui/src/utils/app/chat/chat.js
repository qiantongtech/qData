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

/**
 * 渲染内容
 * @param content
 * @returns {*|string}
 */
export const renderContent = (content) => {
    const startTag = '<think';
    const endTag = '</think>';
    const startIndex = content.indexOf(startTag);

    if (startIndex === -1) {
        return content
    }

    const afterStart = content.substring(startIndex);
    const endIndex = afterStart.indexOf(endTag);

    let remainingContent = '';

    if (endIndex !== -1) {
        const end = endIndex + endTag.length;
        remainingContent = content.substring(0, startIndex) + afterStart.substring(end);
    } else {
        remainingContent = content.substring(0, startIndex);
    }
    return remainingContent;
}

export const getFileFormat = (filename) => {
    if (!filename){
        return  ''
    }
    // 获取最后一个点的位置
    const lastDotIndex = filename.lastIndexOf('.');

    // 如果没有点或点是第一个字符，返回空字符串
    if (lastDotIndex === -1 || lastDotIndex === 0) {
        return '';
    }

    // 提取扩展名并转为小写
    return filename.slice(lastDotIndex + 1).toLowerCase();
}
