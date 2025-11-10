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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.da.dal.mapper.assetchild.audit;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditAlertDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据资产-质量预警Mapper接口
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface DaAssetAuditAlertMapper extends BaseMapperX<DaAssetAuditAlertDO> {

    default PageResult<DaAssetAuditAlertDO> selectPage(DaAssetAuditAlertPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetAuditAlertDO>()
                .eqIfPresent(DaAssetAuditAlertDO::getAssetId, reqVO.getAssetId())
                .eqIfPresent(DaAssetAuditAlertDO::getBatchNo, reqVO.getBatchNo())
                .eqIfPresent(DaAssetAuditAlertDO::getAuditTime, reqVO.getAuditTime())
                .eqIfPresent(DaAssetAuditAlertDO::getAlertTime, reqVO.getAlertTime())
                .eqIfPresent(DaAssetAuditAlertDO::getAlertMessage, reqVO.getAlertMessage())
                .eqIfPresent(DaAssetAuditAlertDO::getAlertChannels, reqVO.getAlertChannels())
                .eqIfPresent(DaAssetAuditAlertDO::getAlertChannelResult, reqVO.getAlertChannelResult())
                .eqIfPresent(DaAssetAuditAlertDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaAssetAuditAlertDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
