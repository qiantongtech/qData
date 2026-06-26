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

package tech.qiantong.qdata.module.ai.service.model;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelSaveReqVO;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelPageReqVO;
import tech.qiantong.qdata.module.ai.dal.dataobject.model.AiModelDO;

/**
 * 模型管理Service接口
 *
 * @author FXB
 * @date 2026-04-01
 */
public interface IAiModelService extends IService<AiModelDO> {

    /**
     * 获得模型管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 模型管理分页列表
     */
    PageResult<AiModelDO> getAiModelPage(AiModelPageReqVO pageReqVO);

    /**
     * 创建模型管理
     *
     * @param createReqVO 模型管理信息
     * @return 模型管理编号
     */
    Long createAiModel(AiModelSaveReqVO createReqVO);

    /**
     * 更新模型管理
     *
     * @param updateReqVO 模型管理信息
     */
    int updateAiModel(AiModelSaveReqVO updateReqVO);

    /**
     * 删除模型管理
     *
     * @param idList 模型管理编号
     */
    int removeAiModel(Collection<Long> idList);

    /**
     * 获得模型管理详情
     *
     * @param id 模型管理编号
     * @return 模型管理
     */
    AiModelDO getAiModelById(Long id);

    /**
     * 获得全部模型管理列表
     *
     * @return 模型管理列表
     */
    List<AiModelDO> getAiModelList();

    /**
     * 获得全部模型管理 Map
     *
     * @return 模型管理 Map
     */
    Map<Long, AiModelDO> getAiModelMap();
}
