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
 */

package tech.qiantong.qdata.module.att.api.client;

import org.springframework.stereotype.Service;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.client.dto.AttClientRespDTO;
import tech.qiantong.qdata.module.att.service.client.IAttClientService;

import javax.annotation.Resource;

/**
 * 应用 Api 实现类
 * @author Ming
 */
@Service
public class ClientApiImpl implements ClientApi {

    @Resource
    private IAttClientService clientService;

    @Override
    public AttClientRespDTO getClient(Long id) {
        return BeanUtils.toBean(clientService.getAttClientById(id), AttClientRespDTO.class);
    }
}
