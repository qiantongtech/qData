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

package tech.qiantong.qdata.module.att.api.service.cat.tagRel;

import tech.qiantong.qdata.module.att.api.Rel.dto.AttTagAssetRelReqDTO;
import tech.qiantong.qdata.module.att.api.Rel.dto.AttTagAssetRelRespDTO;

import java.util.List;

public interface IAttTagAssetRelApiService {

    /**
     * 获得全部类目关联资产信息
     *
     * @return 数据类目管理列表
     */
    List<AttTagAssetRelRespDTO> getApiList(AttTagAssetRelReqDTO attApiCatReqDTO);
    void deleteRelByUpdateTag(Long assetId);
}
