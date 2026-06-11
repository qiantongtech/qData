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

package tech.qiantong.qdata.module.att.api.service.cat.tag;

import tech.qiantong.qdata.module.att.api.Tag.dto.AttTagRespDTO;

import java.util.List;

public interface IAttTagApiService {

    /**
     * 获得全部标签信息
     *
     * @return 数据类目管理列表
     */
    List<AttTagRespDTO> getApiList();
}
