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

package tech.qiantong.qdata.module.att.dal.mapper.Tag;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.Tag.AttTagDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Tag Management Mapper Interface
 *
 * @author qdata
 * @date 2025-07-11
 */
public interface AttTagMapper extends BaseMapperX<AttTagDO> {

    default PageResult<AttTagDO> selectPage(AttTagPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time", "aeest_count"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<AttTagDO>()
                .likeIfPresent(AttTagDO::getName, reqVO.getName())
                .eqIfPresent(AttTagDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttTagDO::getCatCode, reqVO.getCatCode())
                .eqIfPresent(AttTagDO::getAeestCount, reqVO.getAeestCount())
                .eqIfPresent(AttTagDO::getStatus, reqVO.getStatus())
                .eqIfPresent(AttTagDO::getAlias, reqVO.getAlias())
                .eqIfPresent(AttTagDO::getNearSynonyms, reqVO.getNearSynonyms())
                .eqIfPresent(AttTagDO::getSynonyms, reqVO.getSynonyms())
                .eqIfPresent(AttTagDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(AttTagDO::getCreateBy, reqVO.getCreateBy())
                .notInIfPresent(AttTagDO::getId, reqVO.getIds())
                // If reqVO.getName() is not empty, add exact match condition for name (name = '<name>')
                // .likeIfPresent(AttTagDO::getName, reqVO.getName())
                // Sort by createTime in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(),allowedColumns));
    }


    /**
     * Batch update old CAT_CODE to new CAT_CODE
     *
     * @param oldCatCode old category code
     * @param newCatCode new category code
     * @return number of affected rows
     */
    default int updateCatCode(String oldCatCode, String newCatCode) {
        return this.update(
                null,
                Wrappers.<AttTagDO>lambdaUpdate()
                        .set(AttTagDO::getCatCode, newCatCode)
                        .eq(AttTagDO::getDelFlag, "0")
                        .eq(AttTagDO::getCatCode, oldCatCode)
        );
    }}
