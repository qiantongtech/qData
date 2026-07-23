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

/**
 * render content
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
    // Get the position of the last point
    const lastDotIndex = filename.lastIndexOf('.');

    // If there is no dot or dot is the first character, returns an empty string
    if (lastDotIndex === -1 || lastDotIndex === 0) {
        return '';
    }

    // Extract extension and convert to lower case
    return filename.slice(lastDotIndex + 1).toLowerCase();
}
