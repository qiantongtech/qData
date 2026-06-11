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

package tech.qiantong.qdata.module.da.dal.dataobject.assetchild.file;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.database.core.FileInfo;

import java.util.Date;

/**
 * 数据资产-非结构化数据 DO 对象 DA_ASSET_FILE
 *
 * @author Chaos
 * @date 2025-07-16
 */
@Data
@TableName(value = "DA_ASSET_FILE")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
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
     * 资产id
     */
    private Long assetId;

    /**
     * 文件来源
     */
    private String fileSource;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String fileUrl;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件创建时间
     */
    private Date fileCreateTime;

    /**
     * 文件更新时间
     */
    private Date fileUpdateTime;

    /**
     * 是否有效
     */
    private Boolean validFlag;

    /**
     * 删除标志
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
