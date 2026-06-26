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
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainPageReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 数据域管理Mapper接口
 *
 * @author FXB
 * @date 2026-03-24
 */
public interface DmDataDomainMapper extends BaseMapperX<DmDataDomainDO> {

    default PageResult<DmDataDomainDO> selectPage(DmDataDomainPageReqVO reqVO) {
        MPJLambdaWrapperX<DmDataDomainDO> lambdaWrapper = new MPJLambdaWrapperX<>();

        lambdaWrapper.selectAll(DmDataDomainDO.class)
                .select("u.NICK_NAME AS ownerUserName","u.PHONENUMBER AS ownerUserPhoneNumber")
                .leftJoin("SYSTEM_USER u on t.OWNER_USER_ID = u.USER_ID AND u.DEL_FLAG = '0'");

        lambdaWrapper.likeIfPresent(DmDataDomainDO::getName, reqVO.getName())
                .likeIfPresent(DmDataDomainDO::getEngName, reqVO.getEngName())
                .eqIfPresent(DmDataDomainDO::getOwnerUserId, reqVO.getOwnerUserId())
                .eqIfPresent(DmDataDomainDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DmDataDomainDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DmDataDomainDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(","))
                                .stream().map(e -> "t." + LambdaQueryWrapperX.camelToUnderline(e))
                                .collect(Collectors.toList()) : null);
        return selectJoinPage(reqVO, DmDataDomainDO.class, lambdaWrapper);
    }

    default PageResult<DmDataDomainDO> selectlistBybusinessDomainId(DmDataDomainPageReqVO reqVO) {
        MPJLambdaWrapperX<DmDataDomainDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        lambdaWrapper.selectAll(DmDataDomainDO.class)
                .select("u.NICK_NAME AS ownerUserName", "u.PHONENUMBER AS ownerUserPhoneNumber")
                .leftJoin("DM_BUSINESS_DOMAIN_REL t3 ON t3.DATA_DOMAIN_ID=t.ID  AND t3.DEL_FLAG = '0'")
                .leftJoin("DM_BUSINESS_CATEGORY t4 ON t4.ID=t3.BUSINESS_CATEGORY_ID AND t4.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER u on t.OWNER_USER_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .eq(reqVO.getBusinessDomainId() != null, "t4.ID", reqVO.getBusinessDomainId());
        return selectJoinPage(reqVO, DmDataDomainDO.class, lambdaWrapper);
    }
}
