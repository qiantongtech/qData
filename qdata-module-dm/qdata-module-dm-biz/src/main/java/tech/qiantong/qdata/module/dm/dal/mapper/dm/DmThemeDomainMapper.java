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
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmThemeDomainDO;

import java.util.Arrays;

import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * 主题域管理Mapper接口
 *
 * @author FXB
 * @date 2026-03-24
 */
public interface DmThemeDomainMapper extends BaseMapperX<DmThemeDomainDO> {

    default PageResult<DmThemeDomainDO> selectPage(DmThemeDomainPageReqVO reqVO) {
        MPJLambdaWrapperX<DmThemeDomainDO> lambdaWrapper = new MPJLambdaWrapperX<>();

        lambdaWrapper.selectAll(DmThemeDomainDO.class)
                .select("u.NICK_NAME AS ownerUserName","u.PHONENUMBER AS ownerUserPhoneNumber")
                .leftJoin("SYSTEM_USER u on t.OWNER_USER_ID = u.USER_ID AND u.DEL_FLAG = '0'");

        lambdaWrapper.eqIfPresent(DmThemeDomainDO::getCode, reqVO.getCode())
                .likeIfPresent(DmThemeDomainDO::getName, reqVO.getName())
                .likeIfPresent(DmThemeDomainDO::getEngName, reqVO.getEngName())
                .eqIfPresent(DmThemeDomainDO::getParentId, reqVO.getParentId())
                .eqIfPresent(DmThemeDomainDO::getOwnerUserId, reqVO.getOwnerUserId())
                .eqIfPresent(DmThemeDomainDO::getDataLayerId, reqVO.getDataLayerId())
                .likeIfPresent(DmThemeDomainDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DmThemeDomainDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DmThemeDomainDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(","))
                                .stream().map(e -> "t." +LambdaQueryWrapperX.camelToUnderline(e))
                                .collect(Collectors.toList()) : null);

        return selectJoinPage(reqVO, DmThemeDomainDO.class, lambdaWrapper);
    }

    @Update(value = "update DM_THEME_DOMAIN set VALID_FLAG=#{validFlag} where code like concat(#{prefixCode}, '%')")
    int updateValidFlag(@Param("prefixCode") String prefixCode, @Param("validFlag") Boolean validFlag);
}
