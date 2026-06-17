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

package tech.qiantong.qdata.module.da.api.discovery.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 数据发现任务日志 DTO 对象 DA_DISCOVERY_TASK_LOG
 *
 * @author qdata
 * @date 2025-02-17
 */
@Data
public class DaDiscoveryTaskLogReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 实例名称 */
    private String name;

    /** 节点id */
    private Long nodeId;

    /** 节点编码 */
    private String nodeCode;

    /** 任务名称 */
    private String taskName;

    /** 任务id */
    private Long taskId;

    /** 任务编码 */
    private String taskCode;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 状态 */
    private String status;

    /** 新增表数 */
    private Long newTableCount;

    /** 修改表数 */
    private Long modifiedTableCount;

    /** 删除表数 */
    private Long deletedTableCount;

    /** 联系人 */
    private String contact;

    /** 联系人ID */
    private Long contactId;

    /** 联系电话 */
    private String contactNumber;

    /** 邮箱 */
    private String email;

    /** DolphinScheduler的id */
    private Long dsId;

    /** DolphinScheduler的任务实例id */
    private Long dsTaskInstanceId;

    /** 日志路径 */
    private String path;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
