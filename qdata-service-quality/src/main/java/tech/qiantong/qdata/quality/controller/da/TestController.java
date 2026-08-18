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

package tech.qiantong.qdata.quality.controller.da;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.enums.ExecuteType;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.quality.dal.dataobject.asset.DaAssetDO;
import tech.qiantong.qdata.quality.service.asset.IDaAssetService;
import tech.qiantong.qdata.redis.service.IRedisService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <P>
 * Purpose:
 * </p>
 *
 * @author: FXB
 * @create: 2025-07-17 10:30
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IRedisService redisService;

    @Resource
    private IDaAssetService daAssetService;

    @PostMapping("/test2")
    public AjaxResult test2() {
        redisService.set("test", "1", 1200);
        return AjaxResult.success(MessageUtils.messageWithFallback(
                "quality.test.success", "Test succeeded: {0}", redisService.get("test")));
    }

    @PostMapping("/test3")
    public AjaxResult test3() {
        List<DaAssetDO> list = daAssetService.list(Wrappers.lambdaQuery(DaAssetDO.class)
                .eq(DaAssetDO::getId, "198"));
        return AjaxResult.success(list);
    }
}
