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

package tech.qiantong.qdata.module.dpp.service.etl.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceLogDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlTaskInstanceLogMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskInstanceLogService;

import javax.annotation.Resource;

/**
 * Data integration task instance - Log Service business layer processing
 *
 * @author qdata
 * @date 2025-08-05
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlTaskInstanceLogServiceImpl extends ServiceImpl<DppEtlTaskInstanceLogMapper, DppEtlTaskInstanceLogDO> implements IDppEtlTaskInstanceLogService {
    @Resource
    private DppEtlTaskInstanceLogMapper dppEtlTaskInstanceLogMapper;

    @Override
    public boolean saveOrUpdate(DppEtlTaskInstanceLogDO entity) {
        DppEtlTaskInstanceLogDO old = this.getOne(Wrappers.lambdaQuery(DppEtlTaskInstanceLogDO.class)
                .eq(DppEtlTaskInstanceLogDO::getTaskInstanceId, entity.getTaskInstanceId()));
        if (old != null) {
            old.setTm(entity.getTm());
            old.setLogContent(entity.getLogContent());
            return this.update(old, Wrappers.lambdaUpdate(DppEtlTaskInstanceLogDO.class)
                    .eq(DppEtlTaskInstanceLogDO::getTaskInstanceId, entity.getTaskInstanceId()));
        } else {
            return this.save(entity);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public boolean saveOrUpdateRealtime(DppEtlTaskInstanceLogDO entity) {
        return saveOrUpdate(entity);
    }

    @Override
    public String getLog(Long taskInstanceId) {
        DppEtlTaskInstanceLogDO dppEtlTaskInstanceLogDO = this.getOne(Wrappers.lambdaQuery(DppEtlTaskInstanceLogDO.class)
                .eq(DppEtlTaskInstanceLogDO::getTaskInstanceId, taskInstanceId));
        if (dppEtlTaskInstanceLogDO != null) {
            return dppEtlTaskInstanceLogDO.getLogContent();
        }
        return null;
    }
}
