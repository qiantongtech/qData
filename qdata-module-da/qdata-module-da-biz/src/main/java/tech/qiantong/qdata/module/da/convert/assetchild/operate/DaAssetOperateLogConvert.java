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
