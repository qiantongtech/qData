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

package tech.qiantong.qdata.quality.service.asset;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetReqDTO;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetRespDTO;
import tech.qiantong.qdata.quality.dal.dataobject.asset.DaAssetDO;

/**
 * Data asset service interface
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaAssetService extends IService<DaAssetDO> {

    public DaAssetRespDTO insertDaAsset(DaAssetReqDTO daAssetReqDTO);

    public Long getCountByCatCode(String catCode);

    public PageResult<DaAssetRespDTO> daAssetListPage(DaAssetReqDTO daAssetReqDTO);
}
