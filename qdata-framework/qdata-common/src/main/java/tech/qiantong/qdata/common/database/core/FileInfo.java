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

package tech.qiantong.qdata.common.database.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import tech.qiantong.qdata.common.utils.FileTypeUtil;

import java.util.Date;

/**
 * Data assets - unstructured data directories or folders
 *
 * @author Chaos
 * @date 2025-07-16
 */
@Data
public class FileInfo {

    private String name;
    private String path;
    private boolean isDirectory;
    private long size;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lastModified;
    private String type;

    public void fillType(){
        type = isDirectory ? "目录" : FileTypeUtil.getFileType(name);
    }

}
