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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dp.controller.admin.document.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <P>
 * 用途:标准检索响应VO
 * </p>
 *
 * @author: FXB
 * @create: 2025-08-22 10:08
 **/
@Data
public class DpDocumentSearchRespVO implements Serializable {

    private static final long serialVersionUID = -4634002019134354679L;

    /**
     * 数据类型 1:标准，2:逻辑模型，3:数据元，4:代码表
     */
    private String dataType;

    /**
     * ID
     */
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 标准名称
     */
    private String name;

    /**
     * 类目名称（分类名称）
     */
    private String catName;

    /**
     * 文件标准类型字段，;1-国家标准，2-行业标准，3-地方标准，4-团体标准 字典：dp_document_type
     */
    private String type;

    /**
     * 文件状态（标准状态），字典：	dp_document_status
     */
    private String status;

    /**
     * 发布日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    /**
     * 实施日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date implementationDate;

    /**
     * 文件url
     */
    private String fileUrl;

    /**
     * 文件名称
     */
    private String fileName;
}
