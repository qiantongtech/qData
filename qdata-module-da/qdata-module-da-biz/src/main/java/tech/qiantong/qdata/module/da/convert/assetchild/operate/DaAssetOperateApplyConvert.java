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

package tech.qiantong.qdata.module.da.convert.assetchild.operate;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplySaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateApplyDO;

import java.util.List;

/**
 * 数据资产操作申请 Convert
 *
 * @author qdata
 * @date 2025-05-09
 */
@Mapper
public interface DaAssetOperateApplyConvert {
    DaAssetOperateApplyConvert INSTANCE = Mappers.getMapper(DaAssetOperateApplyConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetOperateApplyPageReqVO 请求参数
     * @return DaAssetOperateApplyDO
     */
     DaAssetOperateApplyDO convertToDO(DaAssetOperateApplyPageReqVO daAssetOperateApplyPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetOperateApplySaveReqVO 保存请求参数
     * @return DaAssetOperateApplyDO
     */
     DaAssetOperateApplyDO convertToDO(DaAssetOperateApplySaveReqVO daAssetOperateApplySaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetOperateApplyDO 实体对象
     * @return DaAssetOperateApplyRespVO
     */
     DaAssetOperateApplyRespVO convertToRespVO(DaAssetOperateApplyDO daAssetOperateApplyDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetOperateApplyDOList 实体对象列表
     * @return List<DaAssetOperateApplyRespVO>
     */
     List<DaAssetOperateApplyRespVO> convertToRespVOList(List<DaAssetOperateApplyDO> daAssetOperateApplyDOList);
}
