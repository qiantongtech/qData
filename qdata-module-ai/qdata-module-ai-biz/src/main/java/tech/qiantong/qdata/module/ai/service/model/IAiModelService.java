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
