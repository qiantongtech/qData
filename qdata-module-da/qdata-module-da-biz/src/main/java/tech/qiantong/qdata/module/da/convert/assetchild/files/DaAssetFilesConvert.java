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
