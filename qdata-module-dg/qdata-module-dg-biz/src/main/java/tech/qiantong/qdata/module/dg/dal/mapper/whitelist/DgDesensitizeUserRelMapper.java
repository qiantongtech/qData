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

package tech.qiantong.qdata.module.dg.dal.mapper.whitelist;

import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 脱敏白名单与用户关联关系Mapper接口
 *
 * @author qdata
 * @date 2026-04-09
 */
public interface DgDesensitizeUserRelMapper extends BaseMapperX<DgDesensitizeUserRelDO> {

    default PageResult<DgDesensitizeUserRelDO> selectPage(DgDesensitizeUserRelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DgDesensitizeUserRelDO>()
                .eqIfPresent(DgDesensitizeUserRelDO::getDesensitizeId, reqVO.getDesensitizeId())
                .eqIfPresent(DgDesensitizeUserRelDO::getUserId, reqVO.getUserId())
                .likeIfPresent(DgDesensitizeUserRelDO::getDesensitizeName, reqVO.getDesensitizeName())
                .likeIfPresent(DgDesensitizeUserRelDO::getUserName, reqVO.getUserName())
                .eqIfPresent(DgDesensitizeUserRelDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(DgDesensitizeUserRelDO::getEffectiveCategory, reqVO.getEffectiveCategory())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DgDesensitizeUserRelDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
