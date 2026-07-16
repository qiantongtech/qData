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

package tech.qiantong.qdata.module.dpp.dal.mapper.qa;

import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import java.util.Arrays;

/**
 * Data Quality Task Mapper
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface DppQualityTaskMapper extends BaseMapperX<DppQualityTaskDO> {

    default PageResult<DppQualityTaskDO> selectPage(DppQualityTaskPageReqVO reqVO) {
        String leftJoin = "ATT_QUALITY_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'";

        MPJLambdaWrapperX<DppQualityTaskDO> lambdaWrapperX = new MPJLambdaWrapperX<>();
        lambdaWrapperX.selectAll(DppQualityTaskDO.class)
                .select("t2.NAME AS catName")
                .select("(SELECT COUNT(*) FROM DPP_QUALITY_TASK_OBJ o WHERE o.TASK_ID = t.ID ) taskObjNum")
                .select("(SELECT COUNT(*) FROM DPP_QUALITY_TASK_EVALUATE e WHERE e.TASK_ID = t.ID ) taskEvaluateNum");
        lambdaWrapperX.leftJoin(leftJoin);
        lambdaWrapperX.likeIfPresent(DppQualityTaskDO::getTaskName, reqVO.getTaskName())
                .eqIfPresent(DppQualityTaskDO::getCatCode, reqVO.getCatCode())
                .eqIfPresent(DppQualityTaskDO::getContact, reqVO.getContact())
                .eqIfPresent(DppQualityTaskDO::getAssetFlag, "0")
                .eqIfPresent(DppQualityTaskDO::getContactId, reqVO.getContactId())
                .eqIfPresent(DppQualityTaskDO::getContactNumber, reqVO.getContactNumber())
                .eqIfPresent(DppQualityTaskDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DppQualityTaskDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DppQualityTaskDO::getPriority, reqVO.getPriority())
                .eqIfPresent(DppQualityTaskDO::getWorkerGroup, reqVO.getWorkerGroup())
                .eqIfPresent(DppQualityTaskDO::getRetryTimes, reqVO.getRetryTimes())
                .eqIfPresent(DppQualityTaskDO::getRetryInterval, reqVO.getRetryInterval())
                .eqIfPresent(DppQualityTaskDO::getDelayTime, reqVO.getDelayTime())
                .eqIfPresent(DppQualityTaskDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add exact name match condition (name = '<name>')
                // .likeIfPresent(DppQualityTaskDO::getName, reqVO.getName())
                // Order by createTime descending
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);

        // Build dynamic query conditions
        return selectPage(reqVO, lambdaWrapperX);
    }
}
