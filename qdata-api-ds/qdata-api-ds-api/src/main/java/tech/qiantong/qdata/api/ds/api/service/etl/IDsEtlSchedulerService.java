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
