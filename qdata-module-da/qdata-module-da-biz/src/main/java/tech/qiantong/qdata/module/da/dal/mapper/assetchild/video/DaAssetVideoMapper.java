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

package tech.qiantong.qdata.module.da.dal.mapper.assetchild.video;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.video.DaAssetVideoDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据资产-视频数据Mapper接口
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface DaAssetVideoMapper extends BaseMapperX<DaAssetVideoDO> {

    default PageResult<DaAssetVideoDO> selectPage(DaAssetVideoPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetVideoDO>()
                .eqIfPresent(DaAssetVideoDO::getAssetId, reqVO.getAssetId())
                .eqIfPresent(DaAssetVideoDO::getIp, reqVO.getIp())
                .eqIfPresent(DaAssetVideoDO::getPort, reqVO.getPort())
                .eqIfPresent(DaAssetVideoDO::getProtocol, reqVO.getProtocol())
                .eqIfPresent(DaAssetVideoDO::getPlatform, reqVO.getPlatform())
                .eqIfPresent(DaAssetVideoDO::getConfig, reqVO.getConfig())
                .eqIfPresent(DaAssetVideoDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaAssetVideoDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
