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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.quality.service.qa;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluatePageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluateRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluateSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskEvaluateDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据质量任务-评测规则Service接口
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface IDppQualityTaskEvaluateService extends IService<DppQualityTaskEvaluateDO> {

    /**
     * 获得数据质量任务-评测规则分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据质量任务-评测规则分页列表
     */
    PageResult<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluatePage(DppQualityTaskEvaluatePageReqVO pageReqVO);

    /**
     * 创建数据质量任务-评测规则
     *
     * @param createReqVO 数据质量任务-评测规则信息
     * @return 数据质量任务-评测规则编号
     */
    Long createDppQualityTaskEvaluate(DppQualityTaskEvaluateSaveReqVO createReqVO);

    /**
     * 更新数据质量任务-评测规则
     *
     * @param updateReqVO 数据质量任务-评测规则信息
     */
    int updateDppQualityTaskEvaluate(DppQualityTaskEvaluateSaveReqVO updateReqVO);

    /**
     * 删除数据质量任务-评测规则
     *
     * @param idList 数据质量任务-评测规则编号
     */
    int removeDppQualityTaskEvaluate(Collection<Long> idList);

    /**
     * 获得数据质量任务-评测规则详情
     *
     * @param id 数据质量任务-评测规则编号
     * @return 数据质量任务-评测规则
     */
    DppQualityTaskEvaluateDO getDppQualityTaskEvaluateById(Long id);

    List<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateList(List<Long> idList);

    /**
     * 获得全部数据质量任务-评测规则列表
     *
     * @return 数据质量任务-评测规则列表
     */
    List<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateList();

    /**
     * 获得全部数据质量任务-评测规则 Map
     *
     * @return 数据质量任务-评测规则 Map
     */
    Map<Long, DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateMap();


    /**
     * 导入数据质量任务-评测规则数据
     *
     * @param importExcelList 数据质量任务-评测规则数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppQualityTaskEvaluate(List<DppQualityTaskEvaluateRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
