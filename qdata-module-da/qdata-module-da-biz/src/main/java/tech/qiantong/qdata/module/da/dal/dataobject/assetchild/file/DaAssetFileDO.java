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

package tech.qiantong.qdata.module.da.dal.dataobject.assetchild.file;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.database.core.FileInfo;

import java.util.Date;

/**
 * Data Asset - Unstructured Data DO - DA_ASSET_FILE
 *
 * @author Chaos
 * @date 2025-07-16
 */
@Data
@TableName(value = "DA_ASSET_FILE")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Not needed for MySQL and similar databases.
// @KeySequence("DA_ASSET_FILE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetFileDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Asset ID
     */
    private Long assetId;

    /**
     * File Source
     */
    private String fileSource;

    /**
     * File Name
     */
    private String fileName;

    /**
     * File Path
     */
    private String fileUrl;

    /**
     * File Type
     */
    private String fileType;

    /**
     * File Size
     */
    private Long fileSize;

    /**
     * File Creation Time
     */
    private Date fileCreateTime;

    /**
     * File Update Time
     */
    private Date fileUpdateTime;

    /**
     * Valid Flag
     */
    private Boolean validFlag;

    /**
     * Delete Flag
     */
    @TableLogic
    private Boolean delFlag;

    private String remark;

    public FileInfo toFileInfo() {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setDirectory(false);
        fileInfo.setName(fileName);
        fileInfo.setLastModified(fileUpdateTime);
        fileInfo.setPath(fileUrl);
        fileInfo.setSize(fileSize);
        fileInfo.setType(fileType);
        return fileInfo;
    }

}
