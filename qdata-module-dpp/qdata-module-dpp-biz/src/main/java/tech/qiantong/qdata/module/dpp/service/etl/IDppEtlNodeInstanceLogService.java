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

package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceLogDO;

/**
 * 数据集成节点实例-日志Service接口
 *
 * @author qdata
 * @date 2025-08-05
 */
public interface IDppEtlNodeInstanceLogService extends IService<DppEtlNodeInstanceLogDO> {


    String getLog(Long nodeInstanceId);
}
