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

package tech.qiantong.qdata.module.att.api.service.cat;

import tech.qiantong.qdata.module.att.api.cat.dto.AttDataDevCatReqDTO;
import tech.qiantong.qdata.module.att.api.cat.dto.AttDataDevCatRespDTO;

import java.util.List;

public interface IAttDataDevCatApiService {

    /**
     * 获得全部数据开发类目管理列表
     *
     * @return 数据开发类目管理列表
     */
    List<AttDataDevCatRespDTO> getAttDataDevCatApiList(AttDataDevCatReqDTO attDataDevCatReqDTO);
}
