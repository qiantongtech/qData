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

package tech.qiantong.qdata.module.da.dal.mapper.assetchild.operate;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据资产操作记录Mapper接口
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface DaAssetOperateLogMapper extends BaseMapperX<DaAssetOperateLogDO> {

    default PageResult<DaAssetOperateLogDO> selectPage(DaAssetOperateLogPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetOperateLogDO>()
                .eqIfPresent(DaAssetOperateLogDO::getAssetId, reqVO.getAssetId())
                .eqIfPresent(DaAssetOperateLogDO::getDatasourceId, reqVO.getDatasourceId())
                .likeIfPresent(DaAssetOperateLogDO::getTableName, reqVO.getTableName())
                .eqIfPresent(DaAssetOperateLogDO::getTableComment, reqVO.getTableComment())
                .eqIfPresent(DaAssetOperateLogDO::getUpdateWhereMd5, reqVO.getUpdateWhereMd5())
                .eqIfPresent(DaAssetOperateLogDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DaAssetOperateLogDO::getCreatorId, reqVO.getCreatorId())
                .eqIfPresent(DaAssetOperateLogDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaAssetOperateLogDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    default PageResult<DaAssetOperateLogDO> selectPageNew(DaAssetOperateLogPageReqVO reqVO) {

        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        MPJLambdaWrapper<DaAssetOperateLogDO> lambdaWrapper = new MPJLambdaWrapper();

        lambdaWrapper.selectAll(DaAssetOperateLogDO.class)
                .select("u.nick_name AS nickName, u.user_name AS userName , u.phonenumber AS phoneNumber")
                .leftJoin("SYSTEM_USER u on t.user_id = u.user_id")
                .eq( reqVO.getAssetId()!=null ,DaAssetOperateLogDO::getAssetId, reqVO.getAssetId())
                .eq( reqVO.getDatasourceId() != null ,DaAssetOperateLogDO::getDatasourceId, reqVO.getDatasourceId())
                .like(StringUtils.isNotBlank(reqVO.getTableName()),DaAssetOperateLogDO::getTableName, reqVO.getTableName())
                .eq( StringUtils.isNotBlank(reqVO.getTableComment()) ,DaAssetOperateLogDO::getTableComment, reqVO.getTableComment())
                .eq( StringUtils.isNotBlank(reqVO.getUpdateWhereMd5()) ,DaAssetOperateLogDO::getUpdateWhereMd5, reqVO.getUpdateWhereMd5())
                .eq( StringUtils.isNotBlank(reqVO.getStatus()) ,DaAssetOperateLogDO::getStatus, reqVO.getStatus())
                .eq( reqVO.getCreatorId() != null  ,DaAssetOperateLogDO::getCreatorId, reqVO.getCreatorId())
                .between(reqVO.getStartTime() != null && reqVO.getEndTime() != null,
                        DaAssetOperateLogDO::getCreateTime, reqVO.getStartTime(), reqVO.getEndTime());

        return selectJoinPage(reqVO, DaAssetOperateLogDO.class, lambdaWrapper);
    }
}
