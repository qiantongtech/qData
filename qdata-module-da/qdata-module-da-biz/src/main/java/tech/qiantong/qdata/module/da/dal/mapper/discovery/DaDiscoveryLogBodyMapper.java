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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.da.dal.mapper.discovery;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryLogBodyPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryLogBodyDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据发现节点实例-日志Mapper接口
 *
 * @author qdata
 * @date 2025-10-15
 */
public interface DaDiscoveryLogBodyMapper extends BaseMapperX<DaDiscoveryLogBodyDO> {

    default PageResult<DaDiscoveryLogBodyDO> selectPage(DaDiscoveryLogBodyPageReqVO reqVO) {
        // 允许排序字段，防止 SQL 注入
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("tm", "task_id", "create_time", "update_time"));

        return selectPage(reqVO, new LambdaQueryWrapperX<DaDiscoveryLogBodyDO>()
                .eqIfPresent(DaDiscoveryLogBodyDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(DaDiscoveryLogBodyDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(DaDiscoveryLogBodyDO::getDelFlag, reqVO.getDelFlag())
                .likeIfPresent(DaDiscoveryLogBodyDO::getLogContent, reqVO.getLogContent())
                .betweenIfPresent(DaDiscoveryLogBodyDO::getTm, reqVO.getBeginTm(), reqVO.getEndTm())
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
