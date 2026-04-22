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
