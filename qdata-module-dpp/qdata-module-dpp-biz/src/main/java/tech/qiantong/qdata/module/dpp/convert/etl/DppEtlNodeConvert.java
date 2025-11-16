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

package tech.qiantong.qdata.module.dpp.convert.etl;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeDO;

import java.util.List;

/**
 * 数据集成节点 Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlNodeConvert {
    DppEtlNodeConvert INSTANCE = Mappers.getMapper(DppEtlNodeConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlNodePageReqVO 请求参数
     * @return DppEtlNodeDO
     */
     DppEtlNodeDO convertToDO(DppEtlNodePageReqVO dppEtlNodePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlNodeSaveReqVO 保存请求参数
     * @return DppEtlNodeDO
     */
     DppEtlNodeDO convertToDO(DppEtlNodeSaveReqVO dppEtlNodeSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlNodeDO 实体对象
     * @return DppEtlNodeRespVO
     */
     DppEtlNodeRespVO convertToRespVO(DppEtlNodeDO dppEtlNodeDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlNodeDOList 实体对象列表
     * @return List<DppEtlNodeRespVO>
     */
     List<DppEtlNodeRespVO> convertToRespVOList(List<DppEtlNodeDO> dppEtlNodeDOList);
}
