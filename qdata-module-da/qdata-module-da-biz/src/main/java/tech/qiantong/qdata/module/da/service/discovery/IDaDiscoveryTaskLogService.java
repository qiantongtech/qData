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
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTaskLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据发现任务日志Service接口
 *
 * @author qdata
 * @date 2025-02-17
 */
public interface IDaDiscoveryTaskLogService extends IService<DaDiscoveryTaskLogDO> {

    /**
     * 获得数据发现任务日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据发现任务日志分页列表
     */
    PageResult<DaDiscoveryTaskLogDO> getDaDiscoveryTaskLogPage(DaDiscoveryTaskLogPageReqVO pageReqVO);

    /**
     * 创建数据发现任务日志
     *
     * @param createReqVO 数据发现任务日志信息
     * @return 数据发现任务日志编号
     */
    Long createDaDiscoveryTaskLog(DaDiscoveryTaskLogSaveReqVO createReqVO);

    /**
     * 更新数据发现任务日志
     *
     * @param updateReqVO 数据发现任务日志信息
     */
    int updateDaDiscoveryTaskLog(DaDiscoveryTaskLogSaveReqVO updateReqVO);

    /**
     * 删除数据发现任务日志
     *
     * @param idList 数据发现任务日志编号
     */
    int removeDaDiscoveryTaskLog(Collection<Long> idList);

    /**
     * 获得数据发现任务日志详情
     *
     * @param id 数据发现任务日志编号
     * @return 数据发现任务日志
     */
    DaDiscoveryTaskLogDO getDaDiscoveryTaskLogById(Long id);

    /**
     * 获得全部数据发现任务日志列表
     *
     * @return 数据发现任务日志列表
     */
    List<DaDiscoveryTaskLogDO> getDaDiscoveryTaskLogList();

    /**
     * 获得全部数据发现任务日志 Map
     *
     * @return 数据发现任务日志 Map
     */
    Map<Long, DaDiscoveryTaskLogDO> getDaDiscoveryTaskLogMap();


    /**
     * 导入数据发现任务日志数据
     *
     * @param importExcelList 数据发现任务日志数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaDiscoveryTaskLog(List<DaDiscoveryTaskLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    String getLogInfo(Long id);
}
