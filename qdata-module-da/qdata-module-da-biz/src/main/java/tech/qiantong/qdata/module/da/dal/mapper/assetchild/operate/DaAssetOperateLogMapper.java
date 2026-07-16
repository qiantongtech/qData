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

package tech.qiantong.qdata.module.da.dal.mapper.assetchild.operate;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Asset Operation Record Mapper Interface
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface DaAssetOperateLogMapper extends BaseMapperX<DaAssetOperateLogDO> {

    default PageResult<DaAssetOperateLogDO> selectPage(DaAssetOperateLogPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetOperateLogDO>()
                .eqIfPresent(DaAssetOperateLogDO::getAssetId, reqVO.getAssetId())
                .eqIfPresent(DaAssetOperateLogDO::getDatasourceId, reqVO.getDatasourceId())
                .likeIfPresent(DaAssetOperateLogDO::getTableName, reqVO.getTableName())
                .eqIfPresent(DaAssetOperateLogDO::getTableComment, reqVO.getTableComment())
                .eqIfPresent(DaAssetOperateLogDO::getUpdateWhereMd5, reqVO.getUpdateWhereMd5())
                .eqIfPresent(DaAssetOperateLogDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DaAssetOperateLogDO::getCreatorId, reqVO.getCreatorId())
                .eqIfPresent(DaAssetOperateLogDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add exact match condition for name (name = '<name>')
                // .likeIfPresent(DaAssetOperateLogDO::getName, reqVO.getName())
                // Sort by createTime in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    default PageResult<DaAssetOperateLogDO> selectPageNew(DaAssetOperateLogPageReqVO reqVO) {

        // Define sortable fields (prevent SQL injection, must match database column names)
        MPJLambdaWrapper<DaAssetOperateLogDO> lambdaWrapper = new MPJLambdaWrapper();

        lambdaWrapper.selectAll(DaAssetOperateLogDO.class)
                .select("u.nick_name AS nickName, u.user_name AS userName , u.phonenumber AS phoneNumber")
                .leftJoin("SYSTEM_USER u on t.user_id = u.user_id")
                .eq( reqVO.getAssetId()!=null ,DaAssetOperateLogDO::getAssetId, reqVO.getAssetId())
                .eq( reqVO.getDatasourceId() != null ,DaAssetOperateLogDO::getDatasourceId, reqVO.getDatasourceId())
                .like(StringUtils.isNotBlank(reqVO.getTableName()),DaAssetOperateLogDO::getTableName, reqVO.getTableName())
                .eq( StringUtils.isNotBlank(reqVO.getTableComment()) ,DaAssetOperateLogDO::getTableComment, reqVO.getTableComment())
                .eq( StringUtils.isNotBlank(reqVO.getUpdateWhereMd5()) ,DaAssetOperateLogDO::getUpdateWhereMd5, reqVO.getUpdateWhereMd5())
                .eq( StringUtils.isNotBlank(reqVO.getStatus()) ,DaAssetOperateLogDO::getStatus, reqVO.getStatus())
                .eq( reqVO.getCreatorId() != null  ,DaAssetOperateLogDO::getCreatorId, reqVO.getCreatorId())
                .between(reqVO.getStartTime() != null && reqVO.getEndTime() != null,
                        DaAssetOperateLogDO::getCreateTime, reqVO.getStartTime(), reqVO.getEndTime());

        return selectJoinPage(reqVO, DaAssetOperateLogDO.class, lambdaWrapper);
    }
}
