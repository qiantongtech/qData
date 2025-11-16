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

package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskRespDTO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.*;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据集成任务Service接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlTaskService extends IService<DppEtlTaskDO> {

    /**
     * 获得数据集成任务分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成任务分页列表
     */
    PageResult<DppEtlTaskDO> getDppEtlTaskPage(DppEtlTaskPageReqVO pageReqVO);

    /**
     * 创建数据集成任务
     *
     * @param createReqVO 数据集成任务信息
     * @return 数据集成任务编号
     */
    Long createDppEtlTask(DppEtlTaskSaveReqVO createReqVO);

    /**
     * 更新数据集成任务
     *
     * @param updateReqVO 数据集成任务信息
     */
    int updateDppEtlTask(DppEtlTaskSaveReqVO updateReqVO);

    /**
     * 删除数据集成任务
     *
     * @param idList 数据集成任务编号
     */
    int removeDppEtlTask(Collection<Long> idList);

    /**
     * 获得数据集成任务详情
     *
     * @param id 数据集成任务编号
     * @return 数据集成任务
     */
    DppEtlTaskRespVO getDppEtlTaskById(Long id);

    /**
     * 获得全部数据集成任务列表
     *
     * @return 数据集成任务列表
     */
    List<DppEtlTaskDO> getDppEtlTaskList();

    /**
     * 获得全部数据集成任务 Map
     *
     * @return 数据集成任务 Map
     */
    Map<Long, DppEtlTaskDO> getDppEtlTaskMap();


    /**
     * 导入数据集成任务数据
     *
     * @param importExcelList 数据集成任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppEtlTask(List<DppEtlTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);

    PageResult<DppEtlTaskRespVO> getDppEtlTaskPageList(DppEtlTaskPageReqVO dppEtlTask);

    DppEtlTaskSaveReqVO createProcessDefinition(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    Long getNodeUniqueKey(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    Map<String, Object> updateReleaseTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    DppEtlTaskSaveReqVO updateProcessDefinition(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    Map<String, Object> releaseTaskCrontab(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    DppEtlTaskUpdateQueryRespVO getuUpdateQueryInfo(Long id);

    /**
     * 通过任务编码获取任务id
     *
     * @param taskCode
     * @return
     */
    Long getTaskIdByTaskCode(String taskCode);

    /**
     * 通过任务编码获取任务信息
     *
     * @param taskCode
     * @return
     */
    DppEtlTaskRespDTO getTaskByTaskCode(String taskCode);

    List<DppEtlTaskTreeRespVO> getDppEtlTaskListTree(DppEtlTaskPageReqVO dppEtlTask);

    AjaxResult startDppEtlTask(Long id);

    List<DppEtlTaskRespVO> getSubTaskStatusList(DppEtlTaskPageReqVO dppEtlTask);

    Map<String, Object> updateReleaseJobTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    Map<String, Object> updateReleaseSchedule(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    /**
     * 新增ETL任务
     * @param dppEtlNewNodeSaveReqVO
     * @return
     */
    DppEtlTaskSaveReqVO createEtlTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    /**
     * 修改ETL任务
     * @param dppEtlNewNodeSaveReqVO
     * @return
     */
    DppEtlTaskSaveReqVO updateEtlTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);
    DppEtlNewNodeSaveReqVO createEtlTaskFront(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    DppEtlTaskSaveReqVO createEtlTaskFrontPostposition(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);

    DppEtlTaskUpdateQueryRespVO getupdateQueryFront(Long id);

    DppEtlTaskSaveReqVO copyCreateEtl(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO);
}
