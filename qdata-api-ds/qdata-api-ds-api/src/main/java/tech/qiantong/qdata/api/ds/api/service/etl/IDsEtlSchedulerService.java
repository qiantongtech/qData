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

package tech.qiantong.qdata.api.ds.api.service.etl;

import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsSchedulerRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsSchedulerSaveReqDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsSchedulerUpdateReqDTO;

/**
 * <P>
 * 用途:ds调度相关接口
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-21 10:06
 **/
public interface IDsEtlSchedulerService {

    /**
     * 创建调度器 (只有任务发布了才能调用该接口)
     */
    DsSchedulerRespDTO saveScheduler(DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO, String projectCode);

    /**
     * 修改调度器
     */
    DsSchedulerRespDTO updateScheduler(DsSchedulerUpdateReqDTO dsSchedulerUpdateReqDTO, String projectCode);

    /**
     * 上线调度器 (只有任务发布了才能调用该接口)
     *
     * @param projectCode
     * @param id          调度表中dsId
     * @return
     */
    DsStatusRespDTO onlineScheduler(String projectCode, Long id);

    /**
     * 下线调度器 (只有任务发布了才能调用该接口)
     *
     * @param projectCode
     * @param id          调度表中dsId
     * @return
     */
    DsStatusRespDTO offlineScheduler(String projectCode, Long id);


    /**
     * 根据任务编码获取调度信息
     *
     * @param taskCode
     * @return
     */
    DsSchedulerRespDTO getByTaskCode(String projectCode, String taskCode);
}
