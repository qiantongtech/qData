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

package tech.qiantong.qdata.module.da.convert.assetchild.operate;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateLogDO;

import java.util.List;

/**
 * 数据资产操作记录 Convert
 *
 * @author qdata
 * @date 2025-05-09
 */
@Mapper
public interface DaAssetOperateLogConvert {
    DaAssetOperateLogConvert INSTANCE = Mappers.getMapper(DaAssetOperateLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetOperateLogPageReqVO 请求参数
     * @return DaAssetOperateLogDO
     */
     DaAssetOperateLogDO convertToDO(DaAssetOperateLogPageReqVO daAssetOperateLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetOperateLogSaveReqVO 保存请求参数
     * @return DaAssetOperateLogDO
     */
     DaAssetOperateLogDO convertToDO(DaAssetOperateLogSaveReqVO daAssetOperateLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetOperateLogDO 实体对象
     * @return DaAssetOperateLogRespVO
     */
     DaAssetOperateLogRespVO convertToRespVO(DaAssetOperateLogDO daAssetOperateLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetOperateLogDOList 实体对象列表
     * @return List<DaAssetOperateLogRespVO>
     */
     List<DaAssetOperateLogRespVO> convertToRespVOList(List<DaAssetOperateLogDO> daAssetOperateLogDOList);
}
