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

package tech.qiantong.qdata.common.database.dialect;

import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.FileInfo;

import java.util.List;

/**
 * 数据资产-非结构化方言
 *
 * @author Chaos
 * @date 2025-07-16
 */
public interface FileDialect {

    List<FileInfo> getFiles(DbQueryProperty dbQueryProperty, String path);

    void uploadFile(DbQueryProperty dbQueryProperty, String path, MultipartFile file);

}
