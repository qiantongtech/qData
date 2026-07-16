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

package tech.qiantong.qdata.module.da.dal.mapper.assetColumn;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Data Asset Column Mapper Interface
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface DaAssetColumnMapper extends BaseMapperX<DaAssetColumnDO> {

    default PageResult<DaAssetColumnDO> selectPage(DaAssetColumnPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        MPJLambdaWrapper<DaAssetColumnDO> lambdaQueryWrapper = new MPJLambdaWrapper();
        lambdaQueryWrapper.selectAll(DaAssetColumnDO.class)
                .select("t2.SENSITIVE_LEVEl as sensitiveLevelName")
                .leftJoin("DA_SENSITIVE_LEVEL t2 on t.SENSITIVE_LEVEL_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .eq(StringUtils.isNotBlank(reqVO.getAssetId()),DaAssetColumnDO::getAssetId, reqVO.getAssetId())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        // Build dynamic query conditions
        return selectJoinPage(reqVO, DaAssetColumnDO.class, lambdaQueryWrapper);
    }

    int updateDaAssetColumn(DaAssetColumnDO daAssetColumnDO);

    void deleteAssetColumnByAssetId(Long assetId);


    /**
     * Query column attributes by asset details
     */
    default List<DaAssetColumnDO> findByAssetId(Long assetId) {
        LambdaQueryWrapper<DaAssetColumnDO> queryWrapper = Wrappers.<DaAssetColumnDO>lambdaQuery()
                .eq(DaAssetColumnDO::getAssetId, assetId)
                .eq(DaAssetColumnDO::getDelFlag, 0)
                .orderByAsc(DaAssetColumnDO::getId);
        return selectList(queryWrapper);
    }

}
