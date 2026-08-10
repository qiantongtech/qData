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
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceLogDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlNodeInstanceLogMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeInstanceLogService;

import javax.annotation.Resource;

/**
 * Data integration node instance - Log Service business layer processing
 *
 * @author qdata
 * @date 2025-08-05
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlNodeInstanceLogServiceImpl extends ServiceImpl<DppEtlNodeInstanceLogMapper, DppEtlNodeInstanceLogDO> implements IDppEtlNodeInstanceLogService {
    @Resource
    private DppEtlNodeInstanceLogMapper dppEtlNodeInstanceLogMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public boolean saveOrUpdateRealtime(DppEtlNodeInstanceLogDO entity) {
        DppEtlNodeInstanceLogDO old = this.getOne(Wrappers.lambdaQuery(DppEtlNodeInstanceLogDO.class)
                .eq(DppEtlNodeInstanceLogDO::getNodeInstanceId, entity.getNodeInstanceId()));
        if (old == null) {
            return this.save(entity);
        }
        old.setTm(entity.getTm());
        old.setLogContent(entity.getLogContent());
        // 当前 Mapper 未注册 updateById，按节点实例 ID 条件更新。
        return this.update(old, Wrappers.lambdaUpdate(DppEtlNodeInstanceLogDO.class)
                .eq(DppEtlNodeInstanceLogDO::getNodeInstanceId, entity.getNodeInstanceId()));
    }

    @Override
    public String getLog(Long nodeInstanceId) {
        DppEtlNodeInstanceLogDO dppEtlNodeInstanceLogDO = dppEtlNodeInstanceLogMapper.selectOne(Wrappers.lambdaQuery(DppEtlNodeInstanceLogDO.class)
                .eq(DppEtlNodeInstanceLogDO::getNodeInstanceId, nodeInstanceId));
        if (dppEtlNodeInstanceLogDO != null) {
            return dppEtlNodeInstanceLogDO.getLogContent();
        }
        return null;
    }
}
