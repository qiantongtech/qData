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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dpp.service.etl.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceLogDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlNodeInstanceLogMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeInstanceLogService;

import javax.annotation.Resource;

/**
 * 数据集成节点实例-日志Service业务层处理
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
    public String getLog(Long nodeInstanceId) {
        DppEtlNodeInstanceLogDO dppEtlNodeInstanceLogDO = dppEtlNodeInstanceLogMapper.selectOne(Wrappers.lambdaQuery(DppEtlNodeInstanceLogDO.class)
                .eq(DppEtlNodeInstanceLogDO::getNodeInstanceId, nodeInstanceId));
        if (dppEtlNodeInstanceLogDO != null) {
            return dppEtlNodeInstanceLogDO.getLogContent();
        }
        return null;
    }
}
