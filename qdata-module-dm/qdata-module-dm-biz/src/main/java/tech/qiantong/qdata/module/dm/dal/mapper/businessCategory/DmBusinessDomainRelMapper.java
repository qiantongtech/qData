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

package tech.qiantong.qdata.module.dm.dal.mapper.businessCategory;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelPageReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessDomainRelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 业务分类数据域关联关系Mapper接口
 *
 * @author qdata
 * @date 2026-04-12
 */
public interface DmBusinessDomainRelMapper extends BaseMapperX<DmBusinessDomainRelDO> {

    default PageResult<DmBusinessDomainRelDO> selectPage(DmBusinessDomainRelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DmBusinessDomainRelDO>()
                .eqIfPresent(DmBusinessDomainRelDO::getBusinessCategoryId, reqVO.getBusinessCategoryId())
                .eqIfPresent(DmBusinessDomainRelDO::getDataDomainId, reqVO.getDataDomainId())
                .likeIfPresent(DmBusinessDomainRelDO::getBusinessCategoryName, reqVO.getBusinessCategoryName())
                .likeIfPresent(DmBusinessDomainRelDO::getDataDomainName, reqVO.getDataDomainName())
                .eqIfPresent(DmBusinessDomainRelDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(DmBusinessDomainRelDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DmBusinessDomainRelDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(DmBusinessDomainRelDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DmBusinessDomainRelDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
