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

package tech.qiantong.qdata.module.da.convert.sensitiveLevel;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.sensitiveLevel.DaSensitiveLevelDO;

import java.util.List;

/**
 * 敏感等级 Convert
 *
 * @author qdata
 * @date 2025-01-21
 */
@Mapper
public interface DaSensitiveLevelConvert {
    DaSensitiveLevelConvert INSTANCE = Mappers.getMapper(DaSensitiveLevelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daSensitiveLevelPageReqVO 请求参数
     * @return DaSensitiveLevelDO
     */
     DaSensitiveLevelDO convertToDO(DaSensitiveLevelPageReqVO daSensitiveLevelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daSensitiveLevelSaveReqVO 保存请求参数
     * @return DaSensitiveLevelDO
     */
     DaSensitiveLevelDO convertToDO(DaSensitiveLevelSaveReqVO daSensitiveLevelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daSensitiveLevelDO 实体对象
     * @return DaSensitiveLevelRespVO
     */
     DaSensitiveLevelRespVO convertToRespVO(DaSensitiveLevelDO daSensitiveLevelDO);

    /**
     * DOList 转换为 RespVOList
     * @param daSensitiveLevelDOList 实体对象列表
     * @return List<DaSensitiveLevelRespVO>
     */
     List<DaSensitiveLevelRespVO> convertToRespVOList(List<DaSensitiveLevelDO> daSensitiveLevelDOList);
}
