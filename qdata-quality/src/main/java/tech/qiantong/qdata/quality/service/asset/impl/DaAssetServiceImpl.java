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

package tech.qiantong.qdata.quality.service.asset.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetReqDTO;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetRespDTO;
import tech.qiantong.qdata.quality.dal.dataobject.asset.DaAssetDO;
import tech.qiantong.qdata.quality.dal.mapper.asset.DaAssetMapper;
import tech.qiantong.qdata.quality.service.asset.IDaAssetService;

/**
 * 数据资产Service业务层处理
 *
 * @author lhs
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetServiceImpl extends ServiceImpl<DaAssetMapper, DaAssetDO> implements IDaAssetService {

    @Override
    public DaAssetRespDTO insertDaAsset(DaAssetReqDTO daAssetReqDTO) {
        return null;
    }

    @Override
    public Long getCountByCatCode(String catCode) {
        return null;
    }

    @Override
    public PageResult<DaAssetRespDTO> daAssetListPage(DaAssetReqDTO daAssetReqDTO) {
        return null;
    }
}
