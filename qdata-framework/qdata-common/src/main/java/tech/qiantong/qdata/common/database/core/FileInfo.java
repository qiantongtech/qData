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

package tech.qiantong.qdata.common.database.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import tech.qiantong.qdata.common.utils.FileTypeUtil;

import java.util.Date;

/**
 * 数据资产-非结构化数据 目录或文件夹
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
