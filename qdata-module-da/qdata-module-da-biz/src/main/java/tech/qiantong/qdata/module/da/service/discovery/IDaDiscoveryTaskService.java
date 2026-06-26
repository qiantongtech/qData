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

package tech.qiantong.qdata.module.da.service.discovery;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTaskDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据发现任务Service接口
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface IDaDiscoveryTaskService extends IService<DaDiscoveryTaskDO> {

    /**
     * 获得数据发现任务分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据发现任务分页列表
     */
    PageResult<DaDiscoveryTaskDO> getDaDiscoveryTaskPage(DaDiscoveryTaskPageReqVO pageReqVO);
    PageResult<DaDiscoveryTaskRespVO> getDaDiscoveryTaskListPage(DaDiscoveryTaskPageReqVO pageReqVO);

    /**
     * 创建数据发现任务
     *
     * @param createReqVO 数据发现任务信息
     * @return 数据发现任务编号
     */
    Long createDaDiscoveryTask(DaDiscoveryTaskSaveReqVO createReqVO);

    /**
     * 更新数据发现任务
     *
     * @param updateReqVO 数据发现任务信息
     */
    int updateDaDiscoveryTask(DaDiscoveryTaskSaveReqVO updateReqVO);
    int updateDaDiscoveryTask(DaDiscoveryTaskRespVO updateReqVO);

    /**
     * 删除数据发现任务
     *
     * @param idList 数据发现任务编号
     */
    int removeDaDiscoveryTask(Collection<Long> idList);

    /**
     * 获得数据发现任务详情
     *
     * @param id 数据发现任务编号
     * @return 数据发现任务
     */
    DaDiscoveryTaskRespVO getDaDiscoveryTaskById(Long id);

    /**
     * 获得全部数据发现任务列表
     *
     * @return 数据发现任务列表
     */
    List<DaDiscoveryTaskDO> getDaDiscoveryTaskList();

    /**
     * 获得全部数据发现任务 Map
     *
     * @return 数据发现任务 Map
     */
    Map<Long, DaDiscoveryTaskDO> getDaDiscoveryTaskMap();


    /**
     * 导入数据发现任务数据
     *
     * @param importExcelList 数据发现任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaDiscoveryTask(List<DaDiscoveryTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);

    boolean runDaDiscoveryTask(Long taskId);

    boolean updateDaDiscoveryTaskStatus(DaDiscoveryTaskSaveReqVO daDiscoveryTask);

    boolean updateDaDiscoveryTaskCronExpression(DaDiscoveryTaskSaveReqVO daDiscoveryTask);

    AjaxResult startDaDiscoveryTask(Long id);
}
