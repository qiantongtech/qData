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

package tech.qiantong.qdata.module.da.dal.mapper.assetchild.file;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.file.DaAssetFileDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

public interface DaAssetFileMapper extends BaseMapperX<DaAssetFileDO> {

    default DaAssetFileDO selectByAssetId(Long assetId) {
        return selectOne(new LambdaQueryWrapper<DaAssetFileDO>().eq(DaAssetFileDO::getAssetId, assetId).last("limit 1"));
    }

}
