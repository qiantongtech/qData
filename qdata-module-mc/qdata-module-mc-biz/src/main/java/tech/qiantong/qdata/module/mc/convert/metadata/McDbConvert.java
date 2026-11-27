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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.convert.metadata;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

import java.util.List;

/**
 * Database Convert
 *
 * @author qdata
 * @date 2026-02-11
 */
@Mapper
public interface McDbConvert {
    McDbConvert INSTANCE = Mappers.getMapper(McDbConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcDbPageReqVO request parameters
     * @return McDbDO
     */
     McDbDO convertToDO(McDbPageReqVO mcDbPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcDbSaveReqVO save request parameters
     * @return McDbDO
     */
     McDbDO convertToDO(McDbSaveReqVO mcDbSaveReqVO);

    /**
     * DO to RespVO
     * @param mcDbDO entity object
     * @return McDbRespVO
     */
     McDbRespVO convertToRespVO(McDbDO mcDbDO);

    /**
     * DOList to RespVOList
     * @param mcDbDOList entity object list
     * @return List<McDbRespVO>
     */
     List<McDbRespVO> convertToRespVOList(List<McDbDO> mcDbDOList);
}
