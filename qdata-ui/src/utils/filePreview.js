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

export default function handleFilePreview(fileUrl) {
    const rpUrl = import.meta.env.VITE_RP_VIEW_URL;
    const baseUrl = import.meta.env.VITE_APP_BASE_API;
    const appUrl = window.location.origin;

    const fullUrl = `${appUrl}${baseUrl}${fileUrl.trim()}`;
    console.log(fullUrl);
    // Get screen size
    const screenWidth = window.screen.width;
    const screenHeight = window.screen.height;
    // Set the window size to a fraction of the screen size, e.g. 60%
    const width = screenWidth * 0.7;
    const height = screenHeight * 0.7;
    // Calculate the position of the upper left corner when the window is centered
    const left = (screenWidth - width) / 2;
    const top = (screenHeight - height) / 2;
    // Open new window and center
    const newWindow = window.open(rpUrl + "/onlinePreview?url=" + encodeURIComponent(base64Encode(fullUrl)), "", `scrollbars=yes, width=${width}, height=${height}, top=${top}, left=${left}`);
    if (window.focus) {
        newWindow.focus();
    }
}

const base64Encode = (str) => {
    return btoa(
        encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, function (match, p1) {
            return String.fromCharCode("0x" + p1);
        })
    );
};
