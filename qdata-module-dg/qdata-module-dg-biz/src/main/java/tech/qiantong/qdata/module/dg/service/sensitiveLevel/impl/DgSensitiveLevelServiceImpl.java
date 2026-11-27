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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dg.service.sensitiveLevel.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dg.controller.admin.sensitiveLevel.vo.DgSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.sensitiveLevel.vo.DgSensitiveLevelSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.sensitiveLevel.DgSensitiveLevelDO;
import tech.qiantong.qdata.module.dg.dal.mapper.sensitiveLevel.DgSensitiveLevelMapper;
import tech.qiantong.qdata.module.dg.service.sensitiveLevel.IDgSensitiveLevelService;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * Sensitive Level Service Business Layer Processing
 *
 * @author Chaos
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgSensitiveLevelServiceImpl extends ServiceImpl<DgSensitiveLevelMapper, DgSensitiveLevelDO> implements IDgSensitiveLevelService {
    @Resource
    private DgSensitiveLevelMapper mapper;

    @Override
    public PageResult<DgSensitiveLevelDO> getDgSensitiveLevelPage(DgSensitiveLevelPageReqVO pageReqVO) {
        return mapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgSensitiveLevel(DgSensitiveLevelSaveReqVO createReqVO) {
        DgSensitiveLevelDO dictType = BeanUtils.toBean(createReqVO, DgSensitiveLevelDO.class);
        mapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgSensitiveLevel(DgSensitiveLevelSaveReqVO updateReqVO) {
        // Related validation

        // Update sensitive level
        DgSensitiveLevelDO updateObj = BeanUtils.toBean(updateReqVO, DgSensitiveLevelDO.class);
        return mapper.updateById(updateObj);
    }

    @Override
    public int removeDgSensitiveLevel(Collection<Long> idList) {
        // Batch delete sensitive levels
        return mapper.deleteBatchIds(idList);
    }

    @Override
    public DgSensitiveLevelDO getDgSensitiveLevelById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public Boolean updateStatus(Long id, Long status) {
        return this.update(Wrappers.lambdaUpdate(DgSensitiveLevelDO.class)
                .eq(DgSensitiveLevelDO::getId, id)
                .set(DgSensitiveLevelDO::getOnlineFlag, status));
    }
}
