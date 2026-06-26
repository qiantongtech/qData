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

package tech.qiantong.qdata.module.ai.service.model.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelPageReqVO;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelSaveReqVO;
import tech.qiantong.qdata.module.ai.dal.dataobject.model.AiModelDO;
import tech.qiantong.qdata.module.ai.dal.mapper.model.AiModelMapper;
import tech.qiantong.qdata.module.ai.service.model.IAiModelService;

/**
 * 模型管理Service业务层处理
 *
 * @author FXB
 * @date 2026-04-01
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AiModelServiceImpl extends ServiceImpl<AiModelMapper, AiModelDO> implements IAiModelService {
    @Resource
    private AiModelMapper aiModelMapper;

    @Override
    public PageResult<AiModelDO> getAiModelPage(AiModelPageReqVO pageReqVO) {
        return aiModelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAiModel(AiModelSaveReqVO createReqVO) {
        AiModelDO dictType = BeanUtils.toBean(createReqVO, AiModelDO.class);
        aiModelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAiModel(AiModelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新模型管理
        AiModelDO updateObj = BeanUtils.toBean(updateReqVO, AiModelDO.class);
        return aiModelMapper.updateById(updateObj);
    }

    @Override
    public int removeAiModel(Collection<Long> idList) {
        // 批量删除模型管理
        return aiModelMapper.deleteBatchIds(idList);
    }

    @Override
    public AiModelDO getAiModelById(Long id) {
        return aiModelMapper.selectById(id);
    }

    @Override
    public List<AiModelDO> getAiModelList() {
        return aiModelMapper.selectList();
    }

    @Override
    public Map<Long, AiModelDO> getAiModelMap() {
        List<AiModelDO> aiModelList = aiModelMapper.selectList();
        return aiModelList.stream()
                .collect(Collectors.toMap(
                        AiModelDO::getId,
                        aiModelDO -> aiModelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }
}
