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

package tech.qiantong.qdata.api.ds.api.etl;

import lombok.Data;
import tech.qiantong.qdata.api.ds.api.base.DsResultDTO;
import tech.qiantong.qdata.api.ds.api.etl.ds.Schedule;

/**
 * <P>
 * 用途:调度器响应参数DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-21 10:18
 **/
@Data
public class DsSchedulerRespDTO extends DsResultDTO {
    private Schedule data;
}
