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

package tech.qiantong.qdata.module.dg.dal.mapper.dataLevel;

import tech.qiantong.qdata.module.dg.dal.dataobject.dataLevel.DgDataLevelDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 数据分级Mapper接口
 *
 * @author qdata
 * @date 2026-04-03
 */
public interface DgDataLevelMapper extends BaseMapperX<DgDataLevelDO> {

    default PageResult<DgDataLevelDO> selectPage(DgDataLevelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DgDataLevelDO>()
                .likeIfPresent(DgDataLevelDO::getName, reqVO.getName())
                .likeIfPresent(DgDataLevelDO::getShortName, reqVO.getShortName())
                .eqIfPresent(DgDataLevelDO::getSensitiveLevel, reqVO.getSensitiveLevel())
                .eqIfPresent(DgDataLevelDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(DgDataLevelDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DgDataLevelDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(DgDataLevelDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DgDataLevelDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
