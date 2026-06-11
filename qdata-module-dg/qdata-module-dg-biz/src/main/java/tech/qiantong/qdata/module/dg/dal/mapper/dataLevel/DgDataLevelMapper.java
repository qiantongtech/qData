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
