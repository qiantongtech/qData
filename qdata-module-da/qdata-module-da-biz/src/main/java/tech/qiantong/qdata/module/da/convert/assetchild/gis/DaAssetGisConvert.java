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

package tech.qiantong.qdata.module.da.convert.assetchild.gis;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.gis.DaAssetGisDO;

import java.util.List;

/**
 * 数据资产-地理空间服务 Convert
 *
 * @author qdata
 * @date 2025-04-14
 */
@Mapper
public interface DaAssetGisConvert {
    DaAssetGisConvert INSTANCE = Mappers.getMapper(DaAssetGisConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetGisPageReqVO 请求参数
     * @return DaAssetGisDO
     */
     DaAssetGisDO convertToDO(DaAssetGisPageReqVO daAssetGisPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetGisSaveReqVO 保存请求参数
     * @return DaAssetGisDO
     */
     DaAssetGisDO convertToDO(DaAssetGisSaveReqVO daAssetGisSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetGisDO 实体对象
     * @return DaAssetGisRespVO
     */
     DaAssetGisRespVO convertToRespVO(DaAssetGisDO daAssetGisDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetGisDOList 实体对象列表
     * @return List<DaAssetGisRespVO>
     */
     List<DaAssetGisRespVO> convertToRespVOList(List<DaAssetGisDO> daAssetGisDOList);
}
