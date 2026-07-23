package tech.qiantong.qdata.module.mc.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceLogDO;

import java.util.Collection;
import java.util.List;

/**
 * Collection task instance-Log Service interface
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface IMcTaskInstanceLogService extends IService<McTaskInstanceLogDO> {

    /**
     * Obtain collection task instance-log paging list
     *
     * @param pageReqVO paging request
     * @return Collection task instance-log paging list
     */
    PageResult<McTaskInstanceLogDO> getMcTaskInstanceLogPage(McTaskInstanceLogPageReqVO pageReqVO);

    /**
     * Create a collection task instance-log
     *
     * @param createReqVO collection task instance-log information
     * @return collection task instance-log number
     */
    Long createMcTaskInstanceLog(McTaskInstanceLogSaveReqVO createReqVO);

    /**
     * Update collection task instance-log
     *
     * @param updateReqVO collection task instance-log information
     */
    int updateMcTaskInstanceLog(McTaskInstanceLogSaveReqVO updateReqVO);

    /**
     * Delete collection task instance-log
     *
     * @param idList collection task instance-log number
     */
    int removeMcTaskInstanceLog(Collection<Long> idList);

    /**
     * Obtain collection task instance-log details
     *
     * @param id collection task instance-log number
     * @return collection task instance-log
     */
    McTaskInstanceLogDO getMcTaskInstanceLogById(Long id);

    /**
     * Get all collection task instances-log list
     *
     * @return Collection task instance-log list
     */
    List<McTaskInstanceLogDO> getMcTaskInstanceLogList();

    void taskInstanceLogAppend(Long taskInstanceId, Long taskId, String logStr);

    int saveOrUpdateByPk(McTaskInstanceLogDO entity);
}
