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

package tech.qiantong.qdata.module.system.convert.updater;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackPageReqVO;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackRespVO;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackSaveReqVO;
import tech.qiantong.qdata.module.system.dal.dataobject.updater.SystemVersionTrackDO;

/**
 * 版本跟踪 Convert
 *
 * @author qdata
 * @date 2026-08-12
 */
@Mapper
public interface SystemVersionTrackConvert {
    SystemVersionTrackConvert INSTANCE = Mappers.getMapper(SystemVersionTrackConvert.class);

    /**
     * PageReqVO converted to DO
     * @param systemVersionTrackPageReqVO request parameters
     * @return SystemVersionTrackDO
     */
     SystemVersionTrackDO convertToDO(SystemVersionTrackPageReqVO systemVersionTrackPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param systemVersionTrackSaveReqVO Save request parameters
     * @return SystemVersionTrackDO
     */
     SystemVersionTrackDO convertToDO(SystemVersionTrackSaveReqVO systemVersionTrackSaveReqVO);

    /**
     * DO to RespVO
     * @param systemVersionTrackDO entity object
     * @return SystemVersionTrackRespVO
     */
     SystemVersionTrackRespVO convertToRespVO(SystemVersionTrackDO systemVersionTrackDO);

    /**
     * DOList to RespVOList
     * @param systemVersionTrackDOList entity object list
     * @return List<SystemVersionTrackRespVO>
     */
     List<SystemVersionTrackRespVO> convertToRespVOList(List<SystemVersionTrackDO> systemVersionTrackDOList);
}
