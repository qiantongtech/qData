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
import tech.qiantong.qdata.api.ds.api.etl.DsStartTaskReqDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsTaskSaveReqDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsTaskSaveRespDTO;

/**
 * <P>
 * 用途:ds数据集成任务相关接口
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 16:47
 **/
public interface IDsEtlTaskService {
    /**
     * 创建任务
     *
     * @param dsTaskSaveReqDTO
     * @param projectCode      项目编码
     * @return
     */
    DsTaskSaveRespDTO createTask(DsTaskSaveReqDTO dsTaskSaveReqDTO, Long projectCode);

    /**
     * 更新任务
     *
     * @param dsTaskSaveReqDTO
     * @param projectCode      项目编码
     * @param taskCode         任务编码
     * @return
     */
    DsTaskSaveRespDTO updateTask(DsTaskSaveReqDTO dsTaskSaveReqDTO, String projectCode, String taskCode);

    /**
     * 发布或下线任务
     *
     * @param releaseState releaseState 状态 ONLINE：上线 OFFLINE：下线
     * @param projectCode  项目编码
     * @param code         任务编码
     * @return 注：上线后需将调度也上线，下线时接口会处理调度同时进行下线
     */
    DsStatusRespDTO releaseTask(String releaseState, String projectCode, String code);


    /**
     * 删除任务
     *
     * @param projectCode 项目编码
     * @param code        任务编码
     * @return 注：只有下线的任务才能删除
     */
    DsStatusRespDTO deleteTask(String projectCode, String code);

    /**
     * 启动任务
     *
     * @param dsStartTaskReqDTO
     * @param projectCode      项目编码
     * @return
     */
    DsStatusRespDTO startTask(DsStartTaskReqDTO dsStartTaskReqDTO, String projectCode);


    /**
     *
     * @param code
     * @param projectCode
     * @return
     */
    DsTaskSaveRespDTO batchCopy(String code, String projectCode);

}
