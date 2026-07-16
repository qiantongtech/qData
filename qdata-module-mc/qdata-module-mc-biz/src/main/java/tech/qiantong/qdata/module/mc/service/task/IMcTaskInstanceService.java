package tech.qiantong.qdata.module.mc.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstancePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Collection task instance Service interface
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface IMcTaskInstanceService extends IService<McTaskInstanceDO> {

    /**
     * Get the paging list of collection task instances
     *
     * @param pageReqVO paging request
     * @return Paginated list of collection task instances
     */
    PageResult<McTaskInstanceDO> getMcTaskInstancePage(McTaskInstancePageReqVO pageReqVO);

    /**
     * Create a collection task instance
     *
     * @param createReqVO collects task instance information
     * @return collection task instance number
     */
    Long createMcTaskInstance(McTaskInstanceSaveReqVO createReqVO);
    Long createMcTaskInstance(McTaskInstanceDO createReqVO);

    /**
     * Update collection task instance
     *
     * @param updateReqVO collects task instance information
     */
    int updateMcTaskInstance(McTaskInstanceSaveReqVO updateReqVO);

    /**
     * Delete collection task instance
     *
     * @param idList collection task instance number
     */
    int removeMcTaskInstance(Collection<Long> idList);

    /**
     * Get collection task instance details
     *
     * @param id collection task instance number
     * @return collection task instance
     */
    McTaskInstanceDO getMcTaskInstanceById(Long id);

    /**
     * Get collection task instance details
     *
     * @param taskId
     * @return collection task instance
     */
    McTaskInstanceDO getMcTaskInstanceByTaskId(Long taskId);

    /**
     * Get a list of all collection task instances
     *
     * @return collection task instance list
     */
    List<McTaskInstanceDO> getMcTaskInstanceList();

    /**
     * Get all collection task instances Map
     *
     * @return Collection task instance Map
     */
    Map<Long, McTaskInstanceDO> getMcTaskInstanceMap();


    /**
     * Import collection task instance data
     *
     * @param importExcelList Collection task instance data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    String importMcTaskInstance(List<McTaskInstanceRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
