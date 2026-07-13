package tech.qiantong.qdata.module.mc.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskSchedulerDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data integration scheduling information Service interface
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface IMcTaskSchedulerService extends IService<McTaskSchedulerDO> {

    /**
     * Obtain a paginated list of data integration scheduling information
     *
     * @param pageReqVO paging request
     * @return Data integration scheduling information paging list
     */
    PageResult<McTaskSchedulerDO> getMcTaskSchedulerPage(McTaskSchedulerPageReqVO pageReqVO);

    /**
     * Create data integration scheduling information
     *
     * @param createReqVO data integration scheduling information
     * @return Data integration scheduling information number
     */
    Long createMcTaskScheduler(McTaskSchedulerSaveReqVO createReqVO);

    /**
     * Update data integration scheduling information
     *
     * @param updateReqVO data integration scheduling information
     */
    int updateMcTaskScheduler(McTaskSchedulerSaveReqVO updateReqVO);

    /**
     * Delete data integration scheduling information
     *
     * @param idList Data integration scheduling information number
     */
    int removeMcTaskScheduler(Collection<Long> idList);

    /**
     * Get data integration scheduling information details
     *
     * @param id data integration scheduling information number
     * @return data integration scheduling information
     */
    McTaskSchedulerDO getMcTaskSchedulerById(Long id);

    McTaskSchedulerDO getMcTaskSchedulerBytaskId(Long taskId);

    /**
     * Get a list of all data integration scheduling information
     *
     * @return Data integration scheduling information list
     */
    List<McTaskSchedulerDO> getMcTaskSchedulerList();

    /**
     * Get all data integration scheduling information Map
     *
     * @return Data integration scheduling information Map
     */
    Map<Long, McTaskSchedulerDO> getMcTaskSchedulerMap();


    /**
     * Import data integration scheduling information data
     *
     * @param importExcelList Data integration scheduling information data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    String importMcTaskScheduler(List<McTaskSchedulerRespVO> importExcelList, boolean isUpdateSupport, String operName);


    void updateReleaseSchedule(McTaskSchedulerSaveReqVO updateReqVO);
}
