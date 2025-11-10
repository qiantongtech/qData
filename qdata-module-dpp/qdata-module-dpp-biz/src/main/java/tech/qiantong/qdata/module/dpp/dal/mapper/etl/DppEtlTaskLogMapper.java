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

package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据集成任务-日志Mapper接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface DppEtlTaskLogMapper extends BaseMapperX<DppEtlTaskLogDO> {

    default PageResult<DppEtlTaskLogDO> selectPage(DppEtlTaskLogPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DppEtlTaskLogDO>()
                .eqIfPresent(DppEtlTaskLogDO::getType, reqVO.getType())
                .likeIfPresent(DppEtlTaskLogDO::getName, reqVO.getName())
                .eqIfPresent(DppEtlTaskLogDO::getCode, reqVO.getCode())
                .eqIfPresent(DppEtlTaskLogDO::getVersion, reqVO.getVersion())
                .eqIfPresent(DppEtlTaskLogDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(DppEtlTaskLogDO::getProjectCode, reqVO.getProjectCode())
                .eqIfPresent(DppEtlTaskLogDO::getPersonCharge, reqVO.getPersonCharge())
                .eqIfPresent(DppEtlTaskLogDO::getLocations, reqVO.getLocations())
                .eqIfPresent(DppEtlTaskLogDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DppEtlTaskLogDO::getTimeout, reqVO.getTimeout())
                .eqIfPresent(DppEtlTaskLogDO::getExtractionCount, reqVO.getExtractionCount())
                .eqIfPresent(DppEtlTaskLogDO::getWriteCount, reqVO.getWriteCount())
                .eqIfPresent(DppEtlTaskLogDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DppEtlTaskLogDO::getDsId, reqVO.getDsId())
                .eqIfPresent(DppEtlTaskLogDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DppEtlTaskLogDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    /**
     * 根据任务编码获取最大版本号
     *
     * @param taskCode
     * @return
     */
    Integer queryMaxVersionByCode(@Param("taskCode") String taskCode);
}
