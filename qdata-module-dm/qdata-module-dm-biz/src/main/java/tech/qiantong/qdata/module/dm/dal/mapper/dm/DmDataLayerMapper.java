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

import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 数仓分层管理Mapper接口
 *
 * @author FXB
 * @date 2026-03-24
 */
public interface DmDataLayerMapper extends BaseMapperX<DmDataLayerDO> {

    default PageResult<DmDataLayerDO> selectPage(DmDataLayerPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DmDataLayerDO>()
                .likeIfPresent(DmDataLayerDO::getName, reqVO.getName())
                .likeIfPresent(DmDataLayerDO::getEngName, reqVO.getEngName())
                .eqIfPresent(DmDataLayerDO::getOwnerUserId, reqVO.getOwnerUserId())
                .eqIfPresent(DmDataLayerDO::getCategory, reqVO.getCategory())
                .likeIfPresent(DmDataLayerDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DmDataLayerDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DmDataLayerDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
