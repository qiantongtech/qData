package tech.qiantong.qdata.module.mc.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSaveReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSourceTreeRespVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskScopeDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Collection task service interface
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface IMcTaskService extends IService<McTaskDO> {

    /**
     * Get a paginated list of collection tasks
     *
     * @param pageReqVO paging request
     * @return Paginated list of collection tasks
     */
    PageResult<McTaskDO> getMcTaskPage(McTaskPageReqVO pageReqVO);

    /**
     * Create a collection task
     *
     * @param createReqVO collect task information
     * @return collection task number
     */
    Long createMcTask(McTaskSaveReqVO createReqVO);

    /**
     * Update collection tasks
     *
     * @param updateReqVO collect task information
     */
    int updateMcTask(McTaskSaveReqVO updateReqVO);

    /**
     * Delete collection task
     *
     * @param idList collection task number
     */
    int removeMcTask(Collection<Long> idList);

    /**
     * Get collection task details
     *
     * @param id collection task number
     * @return collection task
     */
    McTaskDO getMcTaskById(Long id);
    McTaskRespVO getMcTaskByIdNew(Long id);

    /**
     * Get a list of all collection tasks
     *
     * @return Collection task list
     */
    List<McTaskDO> getMcTaskList();

    /**
     * Get all collection task maps
     *
     * @return Collection task Map
     */
    Map<Long, McTaskDO> getMcTaskMap();


    /**
     * Import collection task data
     *
     * @param importExcelList collection task data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    String importMcTask(List<McTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);


    /**
     * Scheduled task trigger
     * @param taskId
     * @return
     */
    boolean runDaDiscoveryTask(Long taskId);

    /**
     * Get real-time collection range
     *
     * @param id data source id
     * @return real-time collection range list
     */
    List<McTaskScopeDO> getRealtimeMcTaskScopeList(Long id);

    Map<String, Object> updateReleaseJobTask(McTaskSaveReqVO mcTask);

    Map<String, Object> updateReleaseSchedule(McTaskSaveReqVO mcTask);

    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> list);

    Map<String, Object> runJobOnce(McTaskSaveReqVO mcTask);

    /**
     * Get the source system tree structure
     * Level 1: Source System
     * Level 2: Data source
     * Level 3: Database (displayed according to collection mode)
     *
     * @return tree structure list
     */
    List<McTaskSourceTreeRespVO> getSourceSystemTree();
}
