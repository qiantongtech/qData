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

package tech.qiantong.qdata.module.da.convert.daAssetApply;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyRespVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplySaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.daAssetApply.DaAssetApplyDO;

import java.util.List;

/**
 * 数据资产申请 Convert
 *
 * @author shu
 * @date 2025-03-19
 */
@Mapper
public interface DaAssetApplyConvert {
    DaAssetApplyConvert INSTANCE = Mappers.getMapper(DaAssetApplyConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetApplyPageReqVO 请求参数
     * @return DaAssetApplyDO
     */
     DaAssetApplyDO convertToDO(DaAssetApplyPageReqVO daAssetApplyPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetApplySaveReqVO 保存请求参数
     * @return DaAssetApplyDO
     */
     DaAssetApplyDO convertToDO(DaAssetApplySaveReqVO daAssetApplySaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetApplyDO 实体对象
     * @return DaAssetApplyRespVO
     */
     DaAssetApplyRespVO convertToRespVO(DaAssetApplyDO daAssetApplyDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetApplyDOList 实体对象列表
     * @return List<DaAssetApplyRespVO>
     */
     List<DaAssetApplyRespVO> convertToRespVOList(List<DaAssetApplyDO> daAssetApplyDOList);
}
