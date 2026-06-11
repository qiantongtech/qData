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

package tech.qiantong.qdata.module.dpp.api.service.etl;

import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskInstanceRespDTO;

import java.util.List;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-08-29 10:01
 **/
public interface DppEtlTaskInstanceService {
    /**
     * 获取最新的任务实例
     * @param taskIdList
     * @return
     */
    List<DppEtlTaskInstanceRespDTO> getLastTaskInstance(List<Long> taskIdList);
}
