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

package tech.qiantong.qdata.module.att.dal.mapper.cat;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTagCatDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 标签类目管理Mapper接口
 *
 * @author qdata
 * @date 2025-07-11
 */
public interface AttTagCatMapper extends BaseMapperX<AttTagCatDO> {

    default PageResult<AttTagCatDO> selectPage(AttTagCatPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<AttTagCatDO>()
                .likeIfPresent(AttTagCatDO::getName, reqVO.getName())
                .eqIfPresent(AttTagCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttTagCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttTagCatDO::getDescription, reqVO.getDescription())
                .likeRightIfPresent(AttTagCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttTagCatDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(AttTagCatDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    @Update(value = "update ATT_TAG_CAT set VALID_FLAG=#{validFlag} where code like concat(#{prefixCode}, '%')")
    int updateValidFlag(@Param("prefixCode") String prefixCode, @Param("validFlag") Boolean validFlag);

}
