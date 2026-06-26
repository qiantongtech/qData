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

package tech.qiantong.qdata.module.att.api.sourceSystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.sourceSystem.dto.AttSourceSystemRespDTO;
import tech.qiantong.qdata.module.att.api.sourceSystem.service.IAttSourceSystemApiService;
import tech.qiantong.qdata.module.att.dal.dataobject.sourceSystem.AttSourceSystemDO;
import tech.qiantong.qdata.module.att.service.sourceSystem.IAttSourceSystemService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 来源系统 API 实现类
 *
 * @author qdata
 * @date 2026-04-30
 */
@Slf4j
@Service
public class AttSourceSystemApiImpl implements IAttSourceSystemApiService {

    @Resource
    private IAttSourceSystemService attSourceSystemService;

    @Override
    public List<AttSourceSystemRespDTO> getValidSourceSystems() {
        // 获取所有有效的来源系统
        List<AttSourceSystemDO> validSourceSystems = attSourceSystemService.getAttSourceSystemListByValidFlag(true);

        // 转换为 DTO 对象
        return validSourceSystems.stream()
                .map(sourceSystem -> BeanUtils.toBean(sourceSystem, AttSourceSystemRespDTO.class))
                .collect(Collectors.toList());
    }
}
