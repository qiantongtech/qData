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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dm.dal.mapper.dm;

import org.apache.commons.lang3.StringUtils;
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
                .select("u.NICK_NAME AS ownerUserName")
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
                                .stream().map(e -> LambdaQueryWrapperX.camelToUnderline(e))
                                .collect(Collectors.toList()) : null);

        return selectJoinPage(reqVO, DmThemeDomainDO.class, lambdaWrapper);
    }
}
