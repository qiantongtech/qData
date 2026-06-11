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

package tech.qiantong.qdata.quality.dal.mapper.qa;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityLogDO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据质量日志Mapper接口
 *
 * @author qdata
 * @date 2025-07-19
 */
public interface DppQualityLogMapper extends BaseMapperX<DppQualityLogDO> {

    default PageResult<DppQualityLogDO> selectPage(DppQualityLogPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DppQualityLogDO>()
                .likeIfPresent(DppQualityLogDO::getName, reqVO.getName())
                .eqIfPresent(DppQualityLogDO::getSuccessFlag, reqVO.getSuccessFlag())
                .eqIfPresent(DppQualityLogDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(DppQualityLogDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(DppQualityLogDO::getQualityId, reqVO.getQualityId())
                .eqIfPresent(DppQualityLogDO::getScore, reqVO.getScore())
                .eqIfPresent(DppQualityLogDO::getProblemData, reqVO.getProblemData())
                .eqIfPresent(DppQualityLogDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DppQualityLogDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
