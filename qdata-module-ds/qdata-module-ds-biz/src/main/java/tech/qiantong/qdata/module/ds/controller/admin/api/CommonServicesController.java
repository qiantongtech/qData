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

package tech.qiantong.qdata.module.ds.controller.admin.api;


import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.core.domain.AjaxResult;

/**
 * <p>
 * 开放服务拦截下线、不存在、请求方式错误请求，返回错误信息
 * </p>
 * @author lhs
 */

@RestController
@RequestMapping
@CrossOrigin
public class CommonServicesController {

    /**
     * 拦截services开头的所有请求
     *
     * @return
     */
    @RequestMapping("services/**")
    public AjaxResult services() {
        return AjaxResult.error("服务不存在、或者已下线、或者请求方式错误（GET、POST）");
    }

}
