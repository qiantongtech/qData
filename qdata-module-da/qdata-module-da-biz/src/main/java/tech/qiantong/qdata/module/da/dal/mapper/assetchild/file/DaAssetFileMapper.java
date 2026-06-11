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

package tech.qiantong.qdata.module.da.dal.mapper.assetchild.file;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.file.DaAssetFileDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

public interface DaAssetFileMapper extends BaseMapperX<DaAssetFileDO> {

    default DaAssetFileDO selectByAssetId(Long assetId) {
        return selectOne(new LambdaQueryWrapper<DaAssetFileDO>().eq(DaAssetFileDO::getAssetId, assetId).last("limit 1"));
    }

}
