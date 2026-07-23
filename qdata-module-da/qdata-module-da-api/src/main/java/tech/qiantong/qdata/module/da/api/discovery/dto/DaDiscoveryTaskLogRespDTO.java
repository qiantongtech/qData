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

package tech.qiantong.qdata.module.da.api.discovery.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Data Discovery Task Log DTO DA_DISCOVERY_TASK_LOG
 *
 * @author qdata
 * @date 2025-02-17
 */
@Data
public class DaDiscoveryTaskLogRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Instance Name */
    private String name;

    /** Node ID */
    private Long nodeId;

    /** Node Code */
    private String nodeCode;

    /** Task Name */
    private String taskName;

    /** Task ID */
    private Long taskId;

    /** Task Code */
    private String taskCode;

    /** Start Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** End Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** Status */
    private String status;

    /** New Table Count */
    private Long newTableCount;

    /** Modified Table Count */
    private Long modifiedTableCount;

    /** Deleted Table Count */
    private Long deletedTableCount;

    /** Contact */
    private String contact;

    /** Contact ID */
    private Long contactId;

    /** Contact Number */
    private String contactNumber;

    /** Email */
    private String email;

    /** DolphinScheduler ID */
    private Long dsId;

    /** DolphinScheduler Task Instance ID */
    private Long dsTaskInstanceId;

    /** Log Path */
    private String path;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
