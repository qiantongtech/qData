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

package tech.qiantong.qdata.module.ai.convert.model;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelPageReqVO;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelSaveReqVO;
import tech.qiantong.qdata.module.ai.dal.dataobject.model.AiModelDO;

/**
 * 模型管理 Convert
 *
 * @author FXB
 * @date 2026-04-01
 */
@Mapper
public interface AiModelConvert {
    AiModelConvert INSTANCE = Mappers.getMapper(AiModelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param aiModelPageReqVO 请求参数
     * @return AiModelDO
     */
     AiModelDO convertToDO(AiModelPageReqVO aiModelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param aiModelSaveReqVO 保存请求参数
     * @return AiModelDO
     */
     AiModelDO convertToDO(AiModelSaveReqVO aiModelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param aiModelDO 实体对象
     * @return AiModelRespVO
     */
     AiModelRespVO convertToRespVO(AiModelDO aiModelDO);

    /**
     * DOList 转换为 RespVOList
     * @param aiModelDOList 实体对象列表
     * @return List<AiModelRespVO>
     */
     List<AiModelRespVO> convertToRespVOList(List<AiModelDO> aiModelDOList);
}
