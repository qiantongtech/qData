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

package tech.qiantong.qdata.module.system.controller.admin.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.config.AniviaConfig;
import tech.qiantong.qdata.common.utils.StringUtils;

/**
 * Home page
 *
 * @author qdata
 */
@RestController
public class SysIndexController
{
    /** System base configuration */
    @Autowired
    private AniviaConfig qdataConfig;

    /**
     * Access home page, welcome message
     */
    @RequestMapping("/")
    public String index()
    {
        return StringUtils.format("Welcome to {} admin management framework, current version: v{}, please access through the frontend URL.", qdataConfig.getName(), qdataConfig.getVersion());
    }
}
