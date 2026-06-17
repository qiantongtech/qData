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

package tech.qiantong.qdata.module.dm.convert.dm;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;

/**
 * 数据域管理 Convert
 *
 * @author FXB
 * @date 2026-03-24
 */
@Mapper
public interface DmDataDomainConvert {
    DmDataDomainConvert INSTANCE = Mappers.getMapper(DmDataDomainConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dmDataDomainPageReqVO 请求参数
     * @return DmDataDomainDO
     */
     DmDataDomainDO convertToDO(DmDataDomainPageReqVO dmDataDomainPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dmDataDomainSaveReqVO 保存请求参数
     * @return DmDataDomainDO
     */
     DmDataDomainDO convertToDO(DmDataDomainSaveReqVO dmDataDomainSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dmDataDomainDO 实体对象
     * @return DmDataDomainRespVO
     */
     DmDataDomainRespVO convertToRespVO(DmDataDomainDO dmDataDomainDO);

    /**
     * DOList 转换为 RespVOList
     * @param dmDataDomainDOList 实体对象列表
     * @return List<DmDataDomainRespVO>
     */
     List<DmDataDomainRespVO> convertToRespVOList(List<DmDataDomainDO> dmDataDomainDOList);
}
