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

package tech.qiantong.qdata.module.da.convert.assetchild.files;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.files.DaAssetFilesDO;

import java.util.List;

/**
 * 数据资产-文件服务 Convert
 *
 * @author qdata
 * @date 2025-06-26
 */
@Mapper
public interface DaAssetFilesConvert {
    DaAssetFilesConvert INSTANCE = Mappers.getMapper(DaAssetFilesConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetFilesPageReqVO 请求参数
     * @return DaAssetFilesDO
     */
     DaAssetFilesDO convertToDO(DaAssetFilesPageReqVO daAssetFilesPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetFilesSaveReqVO 保存请求参数
     * @return DaAssetFilesDO
     */
     DaAssetFilesDO convertToDO(DaAssetFilesSaveReqVO daAssetFilesSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetFilesDO 实体对象
     * @return DaAssetFilesRespVO
     */
     DaAssetFilesRespVO convertToRespVO(DaAssetFilesDO daAssetFilesDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetFilesDOList 实体对象列表
     * @return List<DaAssetFilesRespVO>
     */
     List<DaAssetFilesRespVO> convertToRespVOList(List<DaAssetFilesDO> daAssetFilesDOList);
}
