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

package tech.qiantong.qdata.module.att.dal.mapper.cat;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttQualityCatDO;
import java.util.Arrays;

import tech.qiantong.qdata.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * Data Quality Category Mapper Interface
 *
 * @author qdata
 * @date 2025-07-19
 */
public interface AttQualityCatMapper extends BaseMapperX<AttQualityCatDO> {

    default PageResult<AttQualityCatDO> selectPage(AttQualityCatPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<AttQualityCatDO>()
                .likeIfPresent(AttQualityCatDO::getName, reqVO.getName())
                .eqIfPresent(AttQualityCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttQualityCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttQualityCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttQualityCatDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttQualityCatDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add exact match condition for name (name = '<name>')
                // .likeIfPresent(AttQualityCatDO::getName, reqVO.getName())
                // Sort by createTime in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    @Update(value = "update ATT_QUALITY_CAT set VALID_FLAG=#{validFlag} where code like concat(#{prefixCode}, '%')")
    int updateValidFlag(@Param("prefixCode") String prefixCode, @Param("validFlag") Boolean validFlag);

}
