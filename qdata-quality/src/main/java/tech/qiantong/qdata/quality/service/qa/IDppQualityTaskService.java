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
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据质量任务Service接口
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface IDppQualityTaskService extends IService<DppQualityTaskDO> {

    /**
     * 获得数据质量任务分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据质量任务分页列表
     */
    PageResult<DppQualityTaskDO> getDppQualityTaskPage(DppQualityTaskPageReqVO pageReqVO);

    /**
     * 创建数据质量任务
     *
     * @param createReqVO 数据质量任务信息
     * @return 数据质量任务编号
     */
    Long createDppQualityTask(DppQualityTaskSaveReqVO createReqVO);

    /**
     * 更新数据质量任务
     *
     * @param updateReqVO 数据质量任务信息
     */
    int updateDppQualityTask(DppQualityTaskSaveReqVO updateReqVO);

    /**
     * 删除数据质量任务
     *
     * @param idList 数据质量任务编号
     */
    int removeDppQualityTask(Collection<Long> idList);

    /**
     * 获得数据质量任务详情
     *
     * @param id 数据质量任务编号
     * @return 数据质量任务
     */
    DppQualityTaskRespVO getDppQualityTaskById(Long id);

    /**
     * 获得全部数据质量任务列表
     *
     * @return 数据质量任务列表
     */
    List<DppQualityTaskDO> getDppQualityTaskList();

    /**
     * 获得全部数据质量任务 Map
     *
     * @return 数据质量任务 Map
     */
    Map<Long, DppQualityTaskDO> getDppQualityTaskMap();


    /**
     * 导入数据质量任务数据
     *
     * @param importExcelList 数据质量任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppQualityTask(List<DppQualityTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
