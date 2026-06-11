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

package tech.qiantong.qdata.module.att.dal.mapper.cat;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttQualityCatDO;
import java.util.Arrays;

import tech.qiantong.qdata.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 数据质量类目Mapper接口
 *
 * @author qdata
 * @date 2025-07-19
 */
public interface AttQualityCatMapper extends BaseMapperX<AttQualityCatDO> {

    default PageResult<AttQualityCatDO> selectPage(AttQualityCatPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<AttQualityCatDO>()
                .likeIfPresent(AttQualityCatDO::getName, reqVO.getName())
                .eqIfPresent(AttQualityCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttQualityCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttQualityCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttQualityCatDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttQualityCatDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(AttQualityCatDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    @Update(value = "update ATT_QUALITY_CAT set VALID_FLAG=#{validFlag} where code like concat(#{prefixCode}, '%')")
    int updateValidFlag(@Param("prefixCode") String prefixCode, @Param("validFlag") Boolean validFlag);

}
