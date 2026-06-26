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

package tech.qiantong.qdata.module.dpp.convert.qa;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskObjDO;

import java.util.List;

/**
 * 数据质量任务-稽查对象 Convert
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Mapper
public interface DppQualityTaskObjConvert {
    DppQualityTaskObjConvert INSTANCE = Mappers.getMapper(DppQualityTaskObjConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppQualityTaskObjPageReqVO 请求参数
     * @return DppQualityTaskObjDO
     */
     DppQualityTaskObjDO convertToDO(DppQualityTaskObjPageReqVO dppQualityTaskObjPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppQualityTaskObjSaveReqVO 保存请求参数
     * @return DppQualityTaskObjDO
     */
     DppQualityTaskObjDO convertToDO(DppQualityTaskObjSaveReqVO dppQualityTaskObjSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppQualityTaskObjDO 实体对象
     * @return DppQualityTaskObjRespVO
     */
     DppQualityTaskObjRespVO convertToRespVO(DppQualityTaskObjDO dppQualityTaskObjDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppQualityTaskObjDOList 实体对象列表
     * @return List<DppQualityTaskObjRespVO>
     */
     List<DppQualityTaskObjRespVO> convertToRespVOList(List<DppQualityTaskObjDO> dppQualityTaskObjDOList);
}
