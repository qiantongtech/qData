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

package tech.qiantong.qdata.api.ds.api.service.etl;

import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.DSExecuteDTO;

/**
 * <P>
 * 用途:执行相关相关接口
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-27 14:29
 **/
public interface IDsEtlExecutorService {
    /**
     * 执行命令
     *
     * @param dsExecuteDTO
     * @param projectCode
     * @return
     */
    DsStatusRespDTO execute(DSExecuteDTO dsExecuteDTO, String projectCode);
}
