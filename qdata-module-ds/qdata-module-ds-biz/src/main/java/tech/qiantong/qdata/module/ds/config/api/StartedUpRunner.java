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

package tech.qiantong.qdata.module.ds.config.api;


import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.enums.DataConstant;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.module.ds.handler.MappingHandlerMapping;
import tech.qiantong.qdata.module.ds.service.api.IDsApiService;

import javax.annotation.Resource;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StartedUpRunner implements ApplicationRunner {

    private final ConfigurableApplicationContext context;
    private final Environment environment;

    @Resource
    private IDsApiService dsApiService;

    @Resource
    private MappingHandlerMapping mappingHandlerMapping;

    @Override
    public void run(ApplicationArguments args) {
        if (context.isActive()) {
            // Initialize published APIs when the application starts.
            List<DsApiDO> list = dsApiService.lambdaQuery()
                    .eq(DsApiDO::getStatus, DataConstant.ApiState.WAIT.getKey())
                    .list();
            if (CollUtil.isNotEmpty(list)) {
                list.forEach(api -> mappingHandlerMapping.registerMapping(api));
            }
        }
    }
}
