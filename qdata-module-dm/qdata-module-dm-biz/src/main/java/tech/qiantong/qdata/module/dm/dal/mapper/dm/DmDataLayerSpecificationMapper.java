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

package tech.qiantong.qdata.module.dm.dal.mapper.dm;

import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerSpecificationDO;

import java.util.Arrays;

import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * 数仓分层-规范管理Mapper接口
 *
 * @author FXB
 * @date 2026-03-24
 */
public interface DmDataLayerSpecificationMapper extends BaseMapperX<DmDataLayerSpecificationDO> {

    default PageResult<DmDataLayerSpecificationDO> selectPage(DmDataLayerSpecificationPageReqVO reqVO) {
        MPJLambdaWrapperX<DmDataLayerSpecificationDO> lambdaWrapper = new MPJLambdaWrapperX<>();

        lambdaWrapper.selectAll(DmDataLayerSpecificationDO.class)
                .select("u.NICK_NAME AS ownerUserName","u.PHONENUMBER AS ownerUserPhoneNumber")
                .leftJoin("SYSTEM_USER u on t.OWNER_USER_ID = u.USER_ID AND u.DEL_FLAG = '0'");

        // 构造动态查询条件
        lambdaWrapper.eqIfPresent(DmDataLayerSpecificationDO::getDataLayerId, reqVO.getDataLayerId())
                .likeIfPresent(DmDataLayerSpecificationDO::getPrefixName, reqVO.getPrefixName())
                .likeIfPresent(DmDataLayerSpecificationDO::getBusinessEngName, reqVO.getBusinessEngName())
                .eqIfPresent(DmDataLayerSpecificationDO::getOwnerUserId, reqVO.getOwnerUserId())
                .eqIfPresent(DmDataLayerSpecificationDO::getStatus, reqVO.getStatus())
                .likeIfPresent(DmDataLayerSpecificationDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DmDataLayerSpecificationDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DmDataLayerSpecificationDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(","))
                                .stream().map(e -> "t." +LambdaQueryWrapperX.camelToUnderline(e))
                                .collect(Collectors.toList()) : null);
        return selectJoinPage(reqVO, DmDataLayerSpecificationDO.class, lambdaWrapper);
    }
}
