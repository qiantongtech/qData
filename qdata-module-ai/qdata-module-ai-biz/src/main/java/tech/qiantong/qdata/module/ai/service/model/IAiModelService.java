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
 * Model management service interface
 *
 * @author FXB
 * @date 2026-04-01
 */
public interface IAiModelService extends IService<AiModelDO> {

    /**
     * Get the model management paginated list
     *
     * @param pageReqVO paging request
     * @return model management paginated list
     */
    PageResult<AiModelDO> getAiModelPage(AiModelPageReqVO pageReqVO);

    /**
     * Create model management
     *
     * @param createReqVO model management information
     * @return model ID
     */
    Long createAiModel(AiModelSaveReqVO createReqVO);

    /**
     * Update model management
     *
     * @param updateReqVO model management information
     */
    int updateAiModel(AiModelSaveReqVO updateReqVO);

    /**
     * Delete model management
     *
     * @param idList model IDs
     */
    int removeAiModel(Collection<Long> idList);

    /**
     * Get model management details
     *
     * @param id model ID
     * @return model management
     */
    AiModelDO getAiModelById(Long id);

    /**
     * Get a list of all model management
     *
     * @return model management list
     */
    List<AiModelDO> getAiModelList();

    /**
     * Get all model management maps
     *
     * @return model management map
     */
    Map<Long, AiModelDO> getAiModelMap();
}
