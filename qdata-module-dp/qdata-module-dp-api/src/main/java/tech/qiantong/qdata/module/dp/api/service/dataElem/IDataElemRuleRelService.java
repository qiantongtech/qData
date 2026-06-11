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

package tech.qiantong.qdata.module.dp.api.service.dataElem;

import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRuleRelRespDTO;

import java.util.Collection;
import java.util.List;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-03 18:06
 **/
public interface IDataElemRuleRelService {
    /**
     * 通过数据元id列表查询数据元规则关联信息
     *
     * @param dataElemIdList
     * @return
     */
    List<DpDataElemRuleRelRespDTO> listByDataElemIdList(Collection<Long> dataElemIdList, String type);
}
